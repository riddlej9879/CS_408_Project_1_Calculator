package com.example.project_1_calculator;

import android.nfc.Tag;
import android.util.Log;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class AbstractModel {
    public static final String TAG = "AbstractModel";
    protected PropertyChangeSupport propertyChangeSupport;

    public AbstractModel() {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        Log.i(TAG, "addPropertyChangeListener");
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        Log.i(TAG, "removePropertyChangeListener");
        propertyChangeSupport.removePropertyChangeListener(listener);
    }
    public void firePropertyChange(String propName, Object oldVal, Object newVal) {
        Log.i(TAG, "firePropertyChange");
        propertyChangeSupport.firePropertyChange(propName, oldVal, newVal);
    }
}
