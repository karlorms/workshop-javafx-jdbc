package com.roger.workshopjavafxjdbc;

import com.roger.workshopjavafxjdbc.util.Alerts;
import com.roger.workshopjavafxjdbc.util.Utils;
import javafx.event.ActionEvent;
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
import javafx.stage.Stage;
import model.services.DepartmentService;
import model.services.SellerService;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MainViewController implements Initializable {

    @FXML
    private MenuItem menuItemSeller;

    @FXML
    private MenuItem menuItemDepartment;

    @FXML
    private MenuItem menuItemAbout;

    @FXML
    public void onMenuItemSellerAction() {
        loadView("SellerListView.fxml", (SellerListViewController controller) -> {
            controller.setSellerService(new SellerService());
            controller.updateTableView();
        });

    }

    @FXML
    public void onMenuItemDepartmentAction() {
        loadView("DepartmentListView.fxml", (DepartmentListViewController controller) -> {
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();
        });
    }

    @FXML
    public void onMenuItemAboutAction() {
        loadDialogAbout("AboutView.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private <T> void loadView(String view, Consumer<T> initParameter) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
        try {
            VBox newVBox = loader.load();

            Scene scene = HelloApplication.getHelloApplicationScene();
            VBox mainVBox = (VBox) ((ScrollPane) scene.getRoot()).getContent();

            Node mainMenu = mainVBox.getChildren().getFirst();
            mainVBox.getChildren().clear();
            mainVBox.getChildren().add(mainMenu);
            mainVBox.getChildren().addAll(newVBox.getChildren());

            T consumer = loader.getController();
            initParameter.accept(consumer);

        } catch (IOException e) {
            Alerts.showAlert("IO Exception",
                    "Error loading view",
                    e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }

    private void loadDialogAbout(String view) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
        try {
            VBox vBox = loader.load();

            Scene scene = new Scene(vBox);
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Workshop");
            stage.getScene().getWindow();
            stage.alwaysOnTopProperty();
            stage.showAndWait();
            //stage.setAlwaysOnTop(true);
        } catch (IOException e) {
            Alerts.showAlert("IO Exception",
                    "Error loading view",
                    e.getMessage(),
                    Alert.AlertType.ERROR);
        }
    }
}
