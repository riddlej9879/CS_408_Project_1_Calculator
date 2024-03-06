package com.example.project_1_calculator;

public class DefaultController extends AbstractController {
    public static final String OUTPUT_PROPERTY = "Text";

    public void changeOutputText(String newText) {
        setModelProperty(OUTPUT_PROPERTY, newText);
    }
}
