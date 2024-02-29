package com.example.project_1_calculator;

public class DefaultController extends AbstractController {
    private DefaultModel model;
    private MainActivity main;
    public DefaultController(MainActivity main) {
        this.main = main;
        model = new DefaultModel();
    }
    public static final String OUTPUT_PROPERTY = "0";
    public void changeElementOutputText(String newText) {
        setModelProperty(OUTPUT_PROPERTY, newText);
    }
}
