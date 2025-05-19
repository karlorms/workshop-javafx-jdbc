package com.roger.workshopjavafxjdbc;

import com.roger.workshopjavafxjdbc.listeners.DataChangeListener;
import com.roger.workshopjavafxjdbc.util.Alerts;
import com.roger.workshopjavafxjdbc.util.Constraints;
import com.roger.workshopjavafxjdbc.util.Utils;
import db.DbException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.services.DepartmentService;
import model.services.SellerService;

import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.Callable;

public class SellerFormController implements Initializable {

    private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

    private Seller seller;

    private SellerService sellerService;

    private DepartmentService departmentService;

    @FXML
    private Label lblId;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker dpBirthDate;

    @FXML
    private TextField txtBaseSalary;

    @FXML
    private ComboBox<Department> cbBoxDepartment;

    @FXML
    private Label lblNameError;

    @FXML
    private Label lblEmailError;

    @FXML
    private Label lblBirthDateError;

    @FXML
    private Label lblBaseSalaryError;

    @FXML
    private Label lblDepartmentError;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnCancel;

    private ObservableList<Department> observableList;

    public void setServices(SellerService sellerService, DepartmentService departmentService){
        this.sellerService = sellerService;
        this.departmentService = departmentService;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void subscribeDataChangeListener(DataChangeListener listener) {
        dataChangeListeners.add(listener);
    }

    public Seller getFormData() {
        Seller obj = new Seller();
        obj.setId(Utils.tryToParseToInt(lblId.getText()));
        ValidationException exception = new ValidationException("Validation error");
        if (txtName.getText().trim().isEmpty() || txtName.getText() == null) {
            exception.addError("name", "Campo Nome está vazio");
        } else {
            obj.setName(txtName.getText());
        }

        if (txtEmail.getText().trim().isEmpty() || txtEmail.getText() == null) {
            exception.addError("email", "Campo Email está vazio");
        } else {
            obj.setEmail(txtEmail.getText());
        }

        if (dpBirthDate.getValue() == null || dpBirthDate == null) {
            exception.addError("birthdate", "Campo Data de nascimento está vazio");
        } else {
            obj.setBirthDate(dpBirthDate.getValue());
        }

        if (txtBaseSalary.getText().trim().isEmpty() || txtBaseSalary == null) {
            exception.addError("basesalary", "Campo Salário está vazio");
        } else {
            obj.setBaseSalary(Double.parseDouble(txtBaseSalary.getText()));
        }

        if (cbBoxDepartment.getValue() == null || cbBoxDepartment.getSelectionModel().isEmpty()) {
            exception.addError("department", "Departamento não foi selecionado");
        } else {
            obj.setDepartment(cbBoxDepartment.getValue());
        }

        if (exception.getErrors().size() > 0) {
            throw exception;
        }
        return obj;
    }

    private int saveOrUpdate(Seller seller) {
        if (lblId.getText() == null || lblId.getText().isEmpty()) {
            return sellerService.insertSeller(seller);
        } else {
            sellerService.updateSeller(seller);
            return Integer.parseInt(lblId.getText());
        }
    }

    private void cleanFields(){
        lblNameError.setText("");
        lblEmailError.setText("");
        lblBirthDateError.setText("");
        lblBaseSalaryError.setText("");
        lblDepartmentError.setText("");
    }

    @FXML
    public void onSaveAction(ActionEvent actionEvent) {
        try {
            if (sellerService == null) {
                throw new IllegalStateException("SellerService is null");
            }
            cleanFields();
            seller = getFormData();
            lblId.setText(String.valueOf(saveOrUpdate(seller)));
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
        //Constraints.setLabelFieldInteger(lblId);
        Constraints.setTextFieldMaxLength(txtName, 70);
        Constraints.setTextFieldMaxLength(txtEmail, 60);
        Constraints.setTextFieldDouble(txtBaseSalary);
        Utils.formatDatePicker(dpBirthDate, "dd/MM/yyyy");

        initializeComboBoxDepartment();
    }

    private void setErroMessages(Map<String, String> erros) {
        Set<String> fields = erros.keySet();

        if (fields.contains("name")) {
            lblNameError.setText(erros.get("name"));
        }
        if (fields.contains("email")) {
            lblEmailError.setText(erros.get("email"));
        }
        if (fields.contains("birthdate")) {
            lblBirthDateError.setText(erros.get("birthdate"));
        }
        if (fields.contains("basesalary")) {
            lblBaseSalaryError.setText(erros.get("basesalary"));
        }
        if (fields.contains("department")) {
            lblDepartmentError.setText(erros.get("department"));
        }
    }

    public void updateFormData() {
        if(sellerService == null){
            throw new IllegalStateException("SellerService is null");
        }
        if (seller.getId() == null) {
            lblId.setText("");
        } else {
            lblId.setText(String.valueOf(seller.getId()));
            txtName.setText(seller.getName());
            txtEmail.setText(seller.getEmail());
            dpBirthDate.setValue(seller.getBirthDate());
            Locale.setDefault(Locale.US);
            txtBaseSalary.setText(String.format("%.2f", seller.getBaseSalary()));
            cbBoxDepartment.setValue(seller.getDepartment());
        }
    }

    public void loadAssociatedObjects(){
        if(departmentService == null) {
            throw new IllegalStateException("DepartmentService is null");
        }
        List<Department> list = departmentService.findAll();
        observableList = FXCollections.observableArrayList(list);
        cbBoxDepartment.setItems(observableList);
    }

    private void initializeComboBoxDepartment() {
        Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
            @Override
            protected void updateItem(Department item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? "" : item.getName());
            }
        };
        cbBoxDepartment.setCellFactory(factory);
        cbBoxDepartment.setButtonCell(factory.call(null));
    }
}