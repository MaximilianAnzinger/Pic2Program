package de.p2l.ui.ingame.camera;

//Code adapted from: https://www.youtube.com/watch?v=oPu42I0HSi4 (25.11.2018)

import de.p2l.R;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.p2l.service.classifier.tfl.TFLiteClassifier;
import de.p2l.service.sharedDataManager.SharedPrefManager;
import de.p2l.service.parser.parser.Input;
import de.p2l.service.parser.parser.InputToImg;

public class CameraUI extends AppCompatActivity {
    private static final String TAG = "CameraUI";
    private TextureView textureView;

    private TFLiteClassifier classifier;

    private String cameraID;
    private CameraDevice cameraDevice;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder captureRequestBuilder;
    private Size imgDimension;
    private ImageReader imageReader;

    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private boolean mFlashSupported;
    private Handler mBackgroundHandler;
    private HandlerThread mBackgroundThread;

    /*
    stateCallBack opens and closes cameraDevice
     */
    CameraDevice.StateCallback stateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            createCameraPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    /*
    textureListener calls function openCamera() for texture
     */
    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            openCamera();
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };
    private ArrayList<Input> inputList;
    private int selectedListPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_ui);

        initializeClassifier();

        inputList = SharedPrefManager.read(this, getString(R.string.inputList_key));
        selectedListPosition = SharedPrefManager.readInt(this, getString(R.string.selected_position_key));

        textureView = (TextureView) findViewById(R.id.cameraTextView);
        textureView.setSurfaceTextureListener(textureListener);

        Button photoBtn = (Button) findViewById(R.id.cameraUIPhotoBtn);
        photoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });
    }

    /*
    creates an instance of TFLiteClassifier
    */
    private void initializeClassifier() {
        try {
            classifier = new TFLiteClassifier(this);
            Log.i(TAG, "Created de.p2r.service.classifier");
        } catch (IOException e) {
            Log.e(TAG, "Could not create de.p2r.service.classifier");
        }
    }

    private void takePicture() {
        if (cameraDevice == null) return;
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            final ImageReader reader = getImageReader(manager);

            List<Surface> outputSurface = new ArrayList<>(2);
            outputSurface.add(reader.getSurface());
            outputSurface.add(new Surface(textureView.getSurfaceTexture()));

            //define captureBuilder
            final CaptureRequest.Builder captureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            captureBuilder.addTarget(reader.getSurface());
            captureBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
            int rotation = 90;
            captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, rotation);

            //returns listener for available images and processes results
            ImageReader.OnImageAvailableListener readerListener = processAvailablePicture(reader);

            reader.setOnImageAvailableListener(readerListener, mBackgroundHandler);
            final CameraCaptureSession.CaptureCallback captureListener = new CameraCaptureSession.CaptureCallback() {
                @Override
                public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {
                    super.onCaptureCompleted(session, request, result);
                    createCameraPreview();
                }
            };

            CameraCaptureSession.StateCallback stateCallbackForSession = getStateCallbackForSession(captureBuilder, captureListener);
            //TODO: hier liegt der fehler
            cameraDevice.createCaptureSession(outputSurface, stateCallbackForSession, mBackgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Camera could not be accessed");
        }
    }

    /*
    returns stateCallback for Creating Capture Session
     */
    @NonNull
    private CameraCaptureSession.StateCallback getStateCallbackForSession(final CaptureRequest.Builder captureBuilder, final CameraCaptureSession.CaptureCallback captureListener) {
        return new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(@NonNull CameraCaptureSession session) {
                try {
                    session.capture(captureBuilder.build(), captureListener, mBackgroundHandler);
                } catch (CameraAccessException e) {
                    Log.e(TAG, "Could not access camera");
                }
            }

            @Override
            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
            }
        };
    }

    private Activity activity = this;

    /*
    returns listener for available images and processes results
     */
    @NonNull
    private ImageReader.OnImageAvailableListener processAvailablePicture(final ImageReader reader) {
        return new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader imageReader) {
                Image image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.capacity()];
                buffer.get(bytes);
                processPicture(bytes);
            }

            private void processPicture(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                final Input input = classifier.classify(bitmap);

                final Dialog inputDialog = new Dialog(CameraUI.this);
                inputDialog.setContentView(R.layout.camera_input_dialog);
                inputDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                ImageView imageView = (ImageView) inputDialog.findViewById(R.id.cameraInputImageView);
                imageView.setBackgroundResource(InputToImg.parse(input));

                //Accept Picture and save it in inputList
                Button acceptBtn = (Button) inputDialog.findViewById(R.id.cameraAcceptBtn);
                acceptBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputList.add(selectedListPosition++, input);
                        Log.i(TAG, "inputList is: " + inputList.toString());
                        inputDialog.hide();
                    }
                });

                //Decline Picture
                Button declineBtn = (Button) inputDialog.findViewById(R.id.cameraDeclineBtn);
                declineBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        inputDialog.hide();
                    }
                });

                inputDialog.show();
            }
        };
    }

    /*
    returns ImageReader with specified width and height
     */
    private ImageReader getImageReader(CameraManager manager) {
        return ImageReader.newInstance(TFLiteClassifier.DIM_IMG_SIZE_X(), TFLiteClassifier.DIM_IMG_SIZE_Y(), ImageFormat.JPEG, 2);
    }

    private void createCameraPreview() {
        try {
            SurfaceTexture texture = textureView.getSurfaceTexture();
            texture.setDefaultBufferSize(imgDimension.getWidth(), imgDimension.getHeight());
            Surface surface = new Surface(texture);

            captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
            captureRequestBuilder.addTarget(surface);

            cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (cameraDevice == null) return;
                    cameraCaptureSession = session;
                    updatePreview();

                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Log.i(TAG, "changed");
                }
            }, null);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Could not access camera");
        }
    }

    private void updatePreview() {
        if (cameraDevice == null) Log.i(TAG, "Error in updatePreview(): cameraDevice is null");
        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);

        try {
            cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Could not access camera");
        }
    }

    /*
    opens camera
     */
    private void openCamera() {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraID = manager.getCameraIdList()[0];
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraID);
            StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            imgDimension = map.getOutputSizes(SurfaceTexture.class)[0];

            //check realtime permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                }, REQUEST_CAMERA_PERMISSION);
                return;
            }
            manager.openCamera(cameraID, stateCallBack, null);
        } catch (CameraAccessException e) {
            Log.e(TAG, "Could not access camera");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            Log.i(TAG, "Requestcode is for camera permission");
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.i(TAG, "Camera permission granted");
                initializeClassifier();
            } else {
                Log.i(TAG, "no permission for camera");
                finish();
            }

        }
    }

    /*
    read update of inputList and reopen camera
     */
    @Override
    protected void onResume() {
        super.onResume();
        inputList = SharedPrefManager.read(this, getString(R.string.inputList_key));
        selectedListPosition = SharedPrefManager.readInt(this, getString(R.string.selected_position_key));
        startBackgroundThread();
        if (textureView.isAvailable()) openCamera();
        else textureView.setSurfaceTextureListener(textureListener);
    }

    /*
    close de.p2r.service.classifier when activity is stopped
    update inputList
     */
    @Override
    protected void onPause() {
        classifier.close();
        SharedPrefManager.write(this, inputList, getString(R.string.inputList_key));
        SharedPrefManager.write(this, selectedListPosition, getString(R.string.selected_position_key));
        stopBackgroundThread();
        super.onPause();
    }

    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            Log.e(TAG, "Could not stopBackgroundThread");
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }
}
