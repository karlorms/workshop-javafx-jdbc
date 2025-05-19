package com.roger.workshopjavafxjdbc.util;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.input.KeyEvent;

import java.util.function.UnaryOperator;

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

    public static void maskDate(DatePicker datePicker){
        datePicker.getEditor().setOnKeyTyped((KeyEvent event) -> {

            if (!datePicker.getEditor().getText().matches("\\d")) {
                event.consume();
            }

            if (event.getCharacter().trim().isEmpty()) {
                switch (datePicker.getEditor().getText().length()) {
                    case 2:
                        datePicker.getEditor().setText(datePicker.getEditor().getText().substring(0, 1));
                        datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
                        break;
                    case 5:
                        datePicker.getEditor().setText(datePicker.getEditor().getText().substring(0, 4));
                        datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
                        break;
                }
            } else if (datePicker.getEditor().getText().length() == 10) {
                event.consume();
            }
            switch (datePicker.getEditor().getText().length()) {
                case 2, 5:
                    datePicker.getEditor().setText(datePicker.getEditor().getText() + "/");
                    datePicker.getEditor().positionCaret(datePicker.getEditor().getText().length());
                    break;
            }
        });
    }

    public static void applyDateMask(DatePicker datePicker) {
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String newText = change.getControlNewText();

            if (!newText.matches("\\d{0,2}(/\\d{0,2})?(/\\d{0,4})?")) {
                return null;
            }

            if (newText.length() > 2) {
                newText = newText.substring(0, 2) + "/" + newText.substring(2);

            }

            if (newText.length() > 5) {
                newText = newText.substring(3, 5) + "/" + newText.substring(5);
            }

            if (newText.length() > 10) {
                return null;
            }

            return change;
        };

        datePicker.getEditor().setTextFormatter(new TextFormatter<>(filter));
    }

}
