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
import model.exceptions.ValidationException;
import model.services.DepartmentService;

import java.net.URL;
import java.util.*;

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

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    public Department getFormData() {
        Department obj = new Department();
        obj.setId(Utils.tryToParseToInt(lblId.getText()));
        ValidationException exception = new ValidationException("Validation error");
        if (txtName.getText().trim().isEmpty() || txtName.getText() == null) {
            exception.addError("name", "Field Name is empty");
        } else {
            obj.setName(txtName.getText());
        }
        if (exception.getErrors().size() > 0) {
            throw exception;
        }
        return obj;
    }

    private int saveOrUpdate(Department department) {
        if (lblId.getText() == null || lblId.getText().isEmpty()) {
            return departmentService.insertDepartment(department);
        } else {
            departmentService.updateDepartment(department);
            return Integer.parseInt(lblId.getText());
        }
    }

    @FXML
    public void onSaveAction(ActionEvent actionEvent) {
        try {
            department = getFormData();
            departmentService = new DepartmentService();
            lblId.setText(String.valueOf(saveOrUpdate(department)));
            notifyDataChangeListeners();
        } catch (ValidationException e) {
            setErroMessages(e.getErrors());
        } catch (DbException e1) {
            Alerts.showAlert("Error saving data", null, e1.getMessage(), Alert.AlertType.ERROR);
        }
    }


    private void notifyDataChangeListeners() {
        for (DataChangeListener listener : dataChangeListeners) {
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

    private void setErroMessages(Map<String, String> erros) {
        Set<String> fields = erros.keySet();

        if (fields.contains("name")) {
            lblError.setText(erros.get("name"));
        }
    }

    public void updateFormData() {
        if (department.getId() == null) {
            lblId.setText("");
        } else {
            lblId.setText(String.valueOf(department.getId()));
            txtName.setText(department.getName());
        }

    }
}