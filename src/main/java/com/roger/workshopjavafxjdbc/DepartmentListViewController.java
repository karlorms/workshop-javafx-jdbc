package com.roger.workshopjavafxjdbc;

import com.roger.workshopjavafxjdbc.listeners.DataChangeListener;
import com.roger.workshopjavafxjdbc.util.Alerts;
import com.roger.workshopjavafxjdbc.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListViewController implements Initializable, DataChangeListener {

    private DepartmentService departmentService;

    @FXML
    private TableView<Department> departmentTableView;

    @FXML
    private TableColumn<Department, Integer> tableColumnId;

    @FXML
    private TableColumn<Department, String> tableColumnName;

    @FXML
    private TableColumn<Department, Department> tableColumnEdit;

    @FXML
    private Button btNew;

    private ObservableList<Department> departmentObservableList;

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @FXML
    public void onBtNewAction(ActionEvent actionEvent){
        Stage parentEstage = Utils.currentStage(actionEvent);
        loadDialogForm(new Department(), "DepartmentForm.fxml", parentEstage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    public void initializeNodes(){
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));

        Stage stage = (Stage) HelloApplication.getHelloApplicationScene().getWindow();
        departmentTableView.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView(){
        if (departmentService == null){
            throw new IllegalStateException("Service is null");
        }
        List<Department> list = departmentService.findAll();
        departmentObservableList = FXCollections.observableArrayList(list);
        departmentTableView.setItems(departmentObservableList);
        initEditButtons();
    }

    private void loadDialogForm(Department obj, String view, Stage parentStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
        try {
            Pane pane = loader.load();

            DepartmentFormController controller = loader.getController();
            controller.setDepartment(obj);
            controller.subscribeDataChangeListener(this);
            controller.updateFormData();
            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.initOwner(parentStage);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setTitle("Cadastro");
            stage.showAndWait();
        } catch (IOException e) {
            Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void initEditButtons(){
        tableColumnEdit.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param ->
                new TableCell<Department, Department>(){

            private final Button button = new Button();

            @Override
            protected void updateItem(Department obj, boolean empty) {
                super.updateItem(obj, empty);

                File img = new File("src/main/resources/logo/edit.png");
                ImageView view = new ImageView(img.toURI().toString());
                button.setGraphic(view);

                if (obj == null) {
                    //setGraphic(button);
                    return;
                }

                setGraphic(button);
                button.setOnAction(event -> loadDialogForm(
                        obj, "DepartmentForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
