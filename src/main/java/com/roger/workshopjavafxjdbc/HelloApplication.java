package com.roger.workshopjavafxjdbc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("MainView.fxml"));
        ScrollPane scrollPane = fxmlLoader.load();
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scene = new Scene(scrollPane);
        stage.setMaximized(true);
        stage.setTitle("Workshop");
        stage.setScene(scene);
        stage.show();
    }

    public static Scene getHelloApplicationScene(){
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
}
