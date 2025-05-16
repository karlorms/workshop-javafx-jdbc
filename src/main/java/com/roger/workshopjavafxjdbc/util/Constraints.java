package com.roger.workshopjavafxjdbc.util;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Constraints {

    public static void setTextFieldInteger(TextField txt) {
        txt.textProperty().addListener((
                observableValue, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d")) {
                txt.setText(oldValue);
            }
        });
    }

    public static void setLabelFieldInteger(Label lbl) {
        lbl.textProperty().addListener((
                observableValue, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d")) {
                lbl.setText(oldValue);
            }
        });
    }

    public static void setTextFieldMaxLength(TextField txt, int max) {
        txt.textProperty().addListener((
                observableValue, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > max) {
                txt.setText(oldValue);
            }
        });
    }

    public static void setTextFieldDouble(TextField txt) {
        txt.textProperty().addListener((
                observableValue, oldValue, newValue) -> {
            if (newValue != null && !newValue.matches("\\d*([\\.]\\d*)?")) {
                txt.setText(oldValue);
            }
        });
    }

    public static void setTextFieldDate(TextField txt) {
        txt.textProperty().addListener((
                observableValue, oldValue, newValue) -> {
                    if (newValue!= null && !newValue.matches("^\\d{2}/\\d{2}/\\d{4}$\n")) {
                        txt.setText(oldValue);
                    }
                });
    }
}
