package com.roger.workshopjavafxjdbc.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Utils {

    public static Stage currentStage(ActionEvent actionEvent) {
        return (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
    }

    public static Integer tryToParseToInt(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void formatDatePicker(DatePicker datePicker, String format) {
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);

            {
                datePicker.setPromptText(format.toLowerCase());
            }

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                }
                return null;
            }
        });
    }

    public static <T> void formatTableColumnDate(TableColumn<T, LocalDate> tableColumn, String format) {
        tableColumn.setCellFactory(column -> {
            return new TableCell<T, LocalDate>() {
                final DateTimeFormatter fmt = DateTimeFormatter.ofPattern(format);

                @Override
                protected void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        setText(fmt.format(item));
                    }
                }
            };
        });
    }

    public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
        tableColumn.setCellFactory(column -> {
            return new TableCell<T, Double>() {

                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        Locale.setDefault(Locale.US);
                        setText(String.format("%." + decimalPlaces + "f", item));
                    }
                }
            };
        });
    }
}
