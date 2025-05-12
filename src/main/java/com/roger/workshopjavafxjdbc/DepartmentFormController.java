package com.roger.workshopjavafxjdbc;

import com.roger.workshopjavafxjdbc.util.Alerts;
import com.roger.workshopjavafxjdbc.util.Constraints;
import com.roger.workshopjavafxjdbc.util.Utils;
import db.DbException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.services.DepartmentService;

import java.net.URL;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private DepartmentService departmentService;

    @FXML
    private Label lblId;

    @FXML
    private TextField txtName;

    @FXML
    private Label lblError;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    @FXML
    public void onSaveAction() {
        try {
            Department department = new Department(Utils.tryToParseToInt(lblId.getText()),
                    txtName.getText());
            departmentService = new DepartmentService();
            int id = departmentService.insertDepartment(department);
            lblId.setText(String.valueOf(id));
        } catch (DbException e){
            Alerts.showAlert("Error", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    @FXML
    public void onCancelAction(ActionEvent actionEvent) {
        Utils.currentStage(actionEvent).close();
        MainViewController main = new MainViewController();
        main.loadView("DepartmentListView.fxml", (DepartmentListViewController controller) -> {
            controller.setDepartmentService(new DepartmentService());
            controller.updateTableView();
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldMaxLength(txtName, 30);
    }
}
