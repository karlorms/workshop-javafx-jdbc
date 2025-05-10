package com.roger.workshopjavafxjdbc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.services.DepartmentService;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentListController implements Initializable {

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
    public void onBtNewAction(){
        System.out.println("onBtNewAction");
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
}
