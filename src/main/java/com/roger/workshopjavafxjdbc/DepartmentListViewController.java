package com.roger.workshopjavafxjdbc;

import com.roger.workshopjavafxjdbc.listeners.DataChangeListener;
import com.roger.workshopjavafxjdbc.util.Alerts;
import com.roger.workshopjavafxjdbc.util.Utils;
import db.DbIntegrityException;
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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
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
    private TableColumn<Department, Department> tableColumnRemove;

    @FXML
    private HBox hBoxTableColum;

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
        initRemoveButtons();
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

    private void deleteDepartment(Department obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
        if (result.get() == ButtonType.OK){
            if (departmentService == null) {
                throw new IllegalStateException("Service is null");
            }
            try {
                departmentService.deleteDepartment(obj);
                updateTableView();
            } catch (DbIntegrityException e) {
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }

        }
    }

    private void initEditButtons(){
        tableColumnEdit.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param ->
                new TableCell<Department, Department>(){

            private final Button btnEdit = new Button();


            @Override
            protected void updateItem(Department obj, boolean empty) {
                super.updateItem(obj, empty);

                File imgEdit = new File("src/main/resources/logo/edit.png");
                ImageView view = new ImageView(imgEdit.toURI().toString());
                btnEdit.setGraphic(view);

                if (obj == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btnEdit);
                btnEdit.setOnAction(event -> loadDialogForm(obj, "DepartmentForm.fxml", Utils.currentStage(event)));
            }
        });
    }

    private void initRemoveButtons(){
        tableColumnRemove.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param ->
                new TableCell<Department, Department>(){

            private final Button btnRemove = new Button();

            @Override
            public void updateItem(Department obj, boolean empty){
                super.updateItem(obj, empty);

                File imgRemove = new File("src/main/resources/logo/remove-x.png");
                ImageView viewRemove = new ImageView(imgRemove.toURI().toString());
                btnRemove.setGraphic(viewRemove);

                if (obj == null){
                    setGraphic(null);
                    return;
                }

                setGraphic(btnRemove);
                btnRemove.setOnAction(event -> deleteDepartment(obj));
            }
        });
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
