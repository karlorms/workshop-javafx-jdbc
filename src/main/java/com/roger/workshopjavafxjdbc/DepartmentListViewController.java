package com.roger.workshopjavafxjdbc;

import com.roger.workshopjavafxjdbc.listeners.DataChangeListener;
import com.roger.workshopjavafxjdbc.util.Alerts;
import com.roger.workshopjavafxjdbc.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

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
    private Button btNew;

    private ObservableList<Department> departmentObservableList;

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    @FXML
    public void onBtNewAction(ActionEvent actionEvent){
        Stage parentEstage = Utils.currentStage(actionEvent);
        loadDialogForm( "DepartmentForm.fxml", parentEstage);
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
    }

    private void loadDialogForm(String view, Stage parentStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
        try {
            Pane pane = loader.load();

            DepartmentFormController controller = loader.getController();
            controller.setDepartment(new Department());
            controller.subscribeDataChangeListener(this);
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

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
