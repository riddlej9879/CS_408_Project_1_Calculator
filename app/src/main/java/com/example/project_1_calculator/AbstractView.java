package com.example.project_1_calculator;

import java.beans.PropertyChangeEvent;

public interface AbstractView {
    public abstract void modelPropertyChange(final PropertyChangeEvent event);
}
