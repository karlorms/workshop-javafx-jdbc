module com.roger.workshopjavafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.roger.workshopjavafxjdbc to javafx.fxml;
    exports com.roger.workshopjavafxjdbc;
}