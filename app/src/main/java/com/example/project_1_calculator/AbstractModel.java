package com.example.project_1_calculator;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractModel {
    protected PropertyChangeSupport propertyChangeSupport;
    public AbstractModel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    public void firePropertyChange(String propName, Object oldVal, Object newVal) {
        propertyChangeSupport.firePropertyChange(propName, oldVal, newVal);
    }
}