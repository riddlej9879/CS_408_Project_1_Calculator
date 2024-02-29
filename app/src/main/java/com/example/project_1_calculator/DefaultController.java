package com.example.project_1_calculator;

import android.util.Log;

public class DefaultController extends AbstractController {
    public static final String TAG = "DefaultControl";
    public static final String OUTPUT_PROPERTY = "0";
    public void changeOutputText(String newText) {
        Log.i(TAG, "changeOutputText");
        setModelProperty(OUTPUT_PROPERTY, newText);
    }
}
