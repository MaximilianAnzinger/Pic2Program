package de.p2l.service.classifier;

// basic interface to describe the provided functionality of a de.p2r.service.classifier

import android.graphics.Bitmap;

public interface Classifier {

    /**
     * classify a given image
     * Input: bitmap of the image to classify
     * Output: the best matching result
     * */
    Result classify(Bitmap bitmap);

    /**
     * close the de.p2r.service.classifier and free the occupied resources
     * */
    void close();
}
