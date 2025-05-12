module com.roger.workshopjavafxjdbc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;


    opens com.roger.workshopjavafxjdbc to javafx.fxml;
    exports com.roger.workshopjavafxjdbc;
    exports model.entities;
}