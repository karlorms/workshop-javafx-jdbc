package com.roger.workshopjavafxjdbc;

import com.roger.workshopjavafxjdbc.listeners.DataChangeListener;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DepartmentFormController implements Initializable {

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    private Department department;

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

    public void setDepartment(Department department){
        this.department = department;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    @FXML
    public void onSaveAction(ActionEvent actionEvent) {
        try {
            department = new Department(Utils.tryToParseToInt(lblId.getText()),
                    txtName.getText());
            departmentService = new DepartmentService();
            lblId.setText(String.valueOf(departmentService.insertDepartment(department)));
            notifyDataChangeListeners();
        } catch (DbException e){
            Alerts.showAlert("Error", null, e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners){
            listener.onDataChanged();
        }
    }

    @FXML
    public void onCancelAction(ActionEvent actionEvent) {
        Utils.currentStage(actionEvent).close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Constraints.setTextFieldMaxLength(txtName, 30);
    }
}
