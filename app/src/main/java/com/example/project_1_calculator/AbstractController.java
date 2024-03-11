package com.example.project_1_calculator;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.reflect.Method;
import java.util.ArrayList;

public abstract class AbstractController implements PropertyChangeListener {
    private ArrayList<AbstractView> views;
    private ArrayList<AbstractModel> models;

    // Constructor
    public AbstractController() {
        views = new ArrayList<>();
        models = new ArrayList<>();
    }

    // Class methods
    public void addModel(AbstractModel model) {
        models.add(model);
        model.addPropertyChangeListener(this);
    }
    public void removeModel(AbstractModel model) {
        models.remove(model);
        model.removePropertyChangeListener(this);
    }
    public void addView(AbstractView view) {
        views.add(view);
    }
    public void removeView(AbstractView view) {
        views.remove(view);
    }
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (AbstractView view : views) {
            view.modelPropertyChange(evt);
        }
    }
    protected void setModelProperty(String propertyName, Object newOutputText) {
        for (AbstractModel model : models) {
            try {
                Method method = model.getClass().getMethod("set" + propertyName, newOutputText.getClass());
                method.invoke(model, newOutputText);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}