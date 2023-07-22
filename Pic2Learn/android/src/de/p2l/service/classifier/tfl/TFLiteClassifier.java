package de.p2l.service.classifier.tfl;

import de.p2l.service.classifier.Classifier;
import de.p2l.service.parser.parser.Input;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.AbstractMap;
import java.util.PriorityQueue;

import org.tensorflow.lite.Interpreter;

/** Classifies images with Tensorflow Lite. */
public class TFLiteClassifier implements Classifier {

    /** Name of the model file stored in Assets. */
    private static final String MODEL_PATH = "tf/graph.lite";

    /** Name of the label file stored in Assets. */
    private static final String LABEL_PATH = "tf/labels.txt";

    /** Number of results to show in the UI. */
    private static final int RESULTS_TO_SHOW = 3;

    /** Dimensions of inputs. */
    private static final int DIM_BATCH_SIZE = 1;

    private static final int DIM_PIXEL_SIZE = 3;

    private static final int DIM_IMG_SIZE_X;
    private static final int DIM_IMG_SIZE_Y;

    static {
        DIM_IMG_SIZE_X = 224;
        DIM_IMG_SIZE_Y = 224;
    }

    private static final int IMAGE_MEAN = 128;
    private static final float IMAGE_STD = 128.0f;

    /** Preallocated buffers for storing image data in. */
    private int[] intValues = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];

    /** An instance of the driver class to run model inference with Tensorflow Lite. */
    private Interpreter tflite;

    /** Labels corresponding to the output of the vision model. */
    private List<String> labelList;

    /** A ByteBuffer to hold image data, to be feed into Tensorflow Lite as inputs. */
    private ByteBuffer imgData;

    /** An array to hold inference results, to be feed into Tensorflow Lite as outputs. */
    private float[][] labelProbArray;
    /** multi-stage low pass filter **/
    private float[][] filterLabelProbArray;
    private static final int FILTER_STAGES = 3;
    private static final float FILTER_FACTOR = 0.4f;

    private PriorityQueue<Map.Entry<String, Float>> sortedLabels =
            new PriorityQueue<>(
                    RESULTS_TO_SHOW,
                    new Comparator<Map.Entry<String, Float>>() {
                        @Override
                        public int compare(Map.Entry<String, Float> o1, Map.Entry<String, Float> o2) {
                            return (o1.getValue()).compareTo(o2.getValue());
                        }
                    });

    private final String LOG_TAG = "TFLiteClassifier";

    private HashMap<String, String> labelTranslation;

    /** Initializes an {@code TFLiteClassifier}. */
    public TFLiteClassifier(Activity activity) throws IOException {
        tflite = new Interpreter(loadModelFile(activity));
        labelTranslation = loadLabelTrans();
        labelList = loadLabelList(activity);
        imgData = ByteBuffer.allocateDirect(4 * DIM_BATCH_SIZE * DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y * DIM_PIXEL_SIZE);
        imgData.order(ByteOrder.nativeOrder());
        labelProbArray = new float[1][labelList.size()];
        filterLabelProbArray = new float[FILTER_STAGES][labelList.size()];

        Log.i(LOG_TAG, "initialized");
    }

    /** Classifies a frame from the preview stream. */
    @Override
    public Input classify(Bitmap bitmap) {
        Log.i(LOG_TAG, "classification: begin");

        convertBitmapToByteBuffer(bitmap);
        tflite.run(imgData, labelProbArray);

        // smooth the results
        //applyFilter();

        //Log.e(LOG_TAG, printTopKLabels());

        Input output = enumOfLabel(getTopLabel());
        Log.i(LOG_TAG, "classification: end");
        return output;
    }

    /** Closes tflite to release resources. */
    @Override
    public void close() {
        Log.i(LOG_TAG, "Classifier was closed");
        tflite.close();
        tflite = null;
    }

    private void applyFilter(){
        int num_labels =  labelList.size();

        // Low pass filter `labelProbArray` into the first stage of the filter.
        for(int j=0; j<num_labels; ++j){
            filterLabelProbArray[0][j] += FILTER_FACTOR*(labelProbArray[0][j] - filterLabelProbArray[0][j]);
            }
        // Low pass filter each stage into the next.
        for (int i=1; i<FILTER_STAGES; ++i){
            for(int j=0; j<num_labels; ++j){
                filterLabelProbArray[i][j] += FILTER_FACTOR*(filterLabelProbArray[i-1][j] - filterLabelProbArray[i][j]);
            }
        }

        // Copy the last stage filter output back to `labelProbArray`.
        for(int j=0; j<num_labels; ++j){
            labelProbArray[0][j] = filterLabelProbArray[FILTER_STAGES-1][j];
        }
    }

    private HashMap<String, String> loadLabelTrans() {
        HashMap<String, String> map = new HashMap<>();
        map.put("forward", "Vorwärts");
        map.put( "backward", "Rückwärts");
        map.put( "left", "Links");
        map.put( "right", "Rechts");
        map.put( "interaction", "Interaktion");
        map.put( "loop", "Schleife");
        map.put( "branch", "Verzweigung");
        map.put( "free", "Frei");
        map.put( "blocked", "Blockiert");
        map.put( "goal", "Ziel");
        map.put( "nogoal", "Kein Ziel");
        map.put( "end", "Blockende");
        return map;
    }

    /** Reads label list from Assets. */
    private List<String> loadLabelList(Activity activity) throws IOException {
        List<String> labelList = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new InputStreamReader(activity.getAssets().open(LABEL_PATH)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }

    /** Memory-map the model file in Assets. */
    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    /** Writes Image data into a {@code ByteBuffer}. */
    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (imgData == null) {
            return;
        }
        imgData.rewind();
        bitmap.getPixels(intValues, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());
        // Convert the image to floating point.
        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
                final int val = intValues[pixel++];
                imgData.putFloat((((val >> 16) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                imgData.putFloat((((val >> 8) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
                imgData.putFloat((((val) & 0xFF)-IMAGE_MEAN)/IMAGE_STD);
            }
        }
    }

    private String getTopLabel() {
        String label = "";
        float value = 0;

        for (int i = 0; i < labelList.size(); ++i) {
            //Log.e(LOG_TAG, labelList.get(i) + "(" + labelProbArray[0][i] + ")");
            if(value < labelProbArray[0][i]) {
                label = labelList.get(i);
                value = labelProbArray[0][i];
            }
        }
        return label;
    }

    public String printTopKLabels() {
        for (int i = 0; i < labelList.size(); ++i) {
            sortedLabels.add(
                    new AbstractMap.SimpleEntry<>(labelList.get(i), labelProbArray[0][i]));
            if (sortedLabels.size() > RESULTS_TO_SHOW) {
                sortedLabels.poll();
            }
        }
        String textToShow = "";
        final int size = sortedLabels.size();
        for (int i = 0; i < size; ++i) {
            Map.Entry<String, Float> label = sortedLabels.poll();
            textToShow = String.format("\n%s: %4.2f",labelTranslation.get(label.getKey()),label.getValue()) + textToShow;
        }
        return textToShow;
    }

    private Input enumOfLabel(String label) {
        //Log.e(LOG_TAG, "Result: " + label);
        switch (label){
            case "forward": return Input.FORWARD;
            case "backward": return  Input.BACKWARD;
            case "left": return Input.LEFTTURN;
            case "right": return Input.RIGHTTURN;
            case "interaction": return Input.INTERACT;
            case "loop": return Input.LOOPKEY;
            case "branch": return Input.BRANCHKEY;
            case "free": return Input.FREEIN;
            case "blocked": return Input.BLOCKEDIN;
            case "goal": return Input.GOALIN;
            case "nogoal": return Input.NOGOALIN;
            case "end": return Input.BLOCKEND;
            default:
                Log.e(LOG_TAG, "NO LABEL MATCHED");
                return Input.FORWARD;
        }
    }

    public static int DIM_IMG_SIZE_X() {
        return DIM_IMG_SIZE_X;
    }

    public static int DIM_IMG_SIZE_Y() {
        return DIM_IMG_SIZE_Y;
    }
}
