package com.roger.workshopjavafxjdbc;

import com.roger.workshopjavafxjdbc.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;

    @FXML
    private MenuItem menuItemDepartment;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction() {
        System.out.println("Seller Action");
    }

    @FXML
    public void onMenuItemDepartmentAction() {
        loadView("DepartmentList.fxml");
    }

    @FXML
    public void onMenuItemAboutAction() {
        //loadView("AboutView.fxml");
        loadViewPopUp("AboutView.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void loadView(String absoluteName) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
        try {
            VBox newVBox = loader.load();

            Scene scene = HelloApplication.getHelloApplicationScene();
            VBox mainVBox = (VBox) ((ScrollPane) scene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().get(0);
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

        } catch (IOException e) {
            Alerts.showAlert("IO Exception",
                    "Error loading view",
                    e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private void loadViewPopUp(String view){
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
        try {
            VBox vBox = loader.load();

            Scene scene = new Scene(vBox);
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.getScene().getWindow();
            stage.alwaysOnTopProperty();
            stage.show();
            //stage.setAlwaysOnTop(true);
        } catch (IOException e) {
            Alerts.showAlert("IO Exception",
                    "Error loading view",
                    e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }
}
