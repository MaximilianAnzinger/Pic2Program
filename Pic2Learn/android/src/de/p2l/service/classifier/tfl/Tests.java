package de.p2l.service.classifier.tfl;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;



import de.p2l.service.parser.parser.Input;

public class Tests {

    private static final String LOG_TAG = "TFLiteClassifier_Test";
    private static final String LOG_SPACER = "";//"\n----------------------------------------------------------------------------------------------------\n";

    private static final String[] TEST_INPUTS = {
            "manal0.jpg", "manal1.jpg", "manal2.jpg", "manal3.jpg", "manal4.jpg",
            "al5.jpg", "al6.jpg", "al7.jpg", "al8.jpg", "al9.jpg",
            "mantri0.jpg", "mantri1.jpg", "mantri2.jpg", "mantri3.jpg", "mantri4.jpg",
            "tri5.jpg", "tri6.jpg", "tri7.jpg", "tri8.jpg", "tri9.jpg"
    };
    private static final Input[] EXPECTED_OUTPUTS = {
            Input.BACKWARD, Input.BACKWARD, Input.BACKWARD, Input.BACKWARD, Input.BACKWARD,
            Input.BACKWARD, Input.BACKWARD, Input.BACKWARD, Input.BACKWARD, Input.BACKWARD,
            Input.FORWARD, Input.FORWARD, Input.FORWARD, Input.FORWARD, Input.FORWARD,
            Input.FORWARD, Input.FORWARD, Input.FORWARD, Input.FORWARD, Input.FORWARD
    };

    private static void testInitAndClose(Activity activity) {
        log(LOG_SPACER + "Test: InitAndClose started" + LOG_SPACER);

        TFLiteClassifier classifier = null;
        try{
            classifier = new TFLiteClassifier(activity);
        }catch(IOException e) {
            Log.e(LOG_TAG, e.toString());
        }

        classifier.close();

        log(LOG_SPACER + "Test: InitAndClose completed");
        log("Result: 1/1 completed" + LOG_SPACER);
    }

    private static void testClassification(Activity activity) {
        log(LOG_SPACER + "Test: Classification started" + LOG_SPACER);

        TFLiteClassifier classifier = null;
        try{
            classifier = new TFLiteClassifier(activity);
        }catch(IOException e) {
            Log.e(LOG_TAG, e.toString());
        }

        int correctResults = 0;
        Input[] outputs = new Input[TEST_INPUTS.length];
        Bitmap bitmap = null;
        for(int i = 0; i<TEST_INPUTS.length; i++) {
            try{
                log("Sample_Image: tf/testImages/" + TEST_INPUTS[i]);
                bitmap = BitmapFactory.decodeStream(activity.getAssets().open("tf/testImages/" + TEST_INPUTS[i]));
                outputs[i] = classifier.classify(bitmap);
            }catch (IOException e){
                Log.e(LOG_TAG, e.toString());
            }
            if(outputs[i] == EXPECTED_OUTPUTS[i]) correctResults ++;
        }

        classifier.close();

        log(LOG_SPACER + "Test: Classification completed\n" + "Result: " + correctResults + "/" + TEST_INPUTS.length + " completed");
        if(correctResults != TEST_INPUTS.length)
            for(int i = 0; i<TEST_INPUTS.length; i++)
                if(outputs[i] != EXPECTED_OUTPUTS[i])
                    log("image: " + TEST_INPUTS[i] + " out: " + outputs[i].toString() + " expected out: " + EXPECTED_OUTPUTS[i].toString());
        log(LOG_SPACER);
    }

    private static void log(String msg) {
        Log.i(LOG_TAG, msg);
    }

    public static void runAll(Activity activity) {
        log(LOG_SPACER + "Test of classifier: BEGIN" + LOG_SPACER);

        testInitAndClose(activity);
        testClassification(activity);

        log(LOG_SPACER + "Test of classifier: END" + LOG_SPACER);
    }
}
