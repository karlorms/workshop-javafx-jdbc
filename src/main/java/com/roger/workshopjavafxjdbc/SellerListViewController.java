package com.roger.workshopjavafxjdbc;

import com.roger.workshopjavafxjdbc.listeners.DataChangeListener;
import com.roger.workshopjavafxjdbc.util.Alerts;
import com.roger.workshopjavafxjdbc.util.Constraints;
import com.roger.workshopjavafxjdbc.util.Utils;
import db.DbIntegrityException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
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
import javafx.util.Callback;
import model.entities.Seller;
import model.services.DepartmentService;
import model.services.SellerService;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class SellerListViewController implements Initializable, DataChangeListener {

    private SellerService sellerService;

    @FXML
    private TableView<Seller> sellerTableView;

    @FXML
    private TableColumn<Seller, Integer> tableColumnId;

    @FXML
    private TableColumn<Seller, String> tableColumnName;

    @FXML
    private TableColumn<Seller, String> tableColumnEmail;

    @FXML
    private TableColumn<Seller, LocalDate> tableColumnBirthDate;

    @FXML
    private TableColumn<Seller, Double> tableColumnBaseSalary;

    @FXML
    private TableColumn<Seller, Seller> tableColumnEdit;

    @FXML
    private TableColumn<Seller, Seller> tableColumnRemove;

    @FXML
    private HBox hBoxTableColum;

    @FXML
    private Button btNew;

    private ObservableList<Seller> sellerObservableList;

    public void setSellerService(SellerService sellerService) {
        this.sellerService = sellerService;
    }

    @FXML
    public void onBtNewAction(ActionEvent actionEvent) {
        Stage parentEstage = Utils.currentStage(actionEvent);
        loadDialogForm(new Seller(), "SellerForm.fxml", parentEstage);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeNodes();
    }

    public void initializeNodes() {
        tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("Email"));
        tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("BirthDate"));
        Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
        tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("BaseSalary"));
        Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);

        Stage stage = (Stage) HelloApplication.getHelloApplicationScene().getWindow();
        sellerTableView.prefHeightProperty().bind(stage.heightProperty());
    }

    public void updateTableView() {
        if (sellerService == null) {
            throw new IllegalStateException("Service is null");
        }
        List<Seller> list = sellerService.findAll();
        sellerObservableList = FXCollections.observableArrayList(list);
        sellerTableView.setItems(sellerObservableList);
        initEditButtons();
        initRemoveButtons();
    }

    private void loadDialogForm(Seller obj, String view, Stage parentStage) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(view));
        try {
            Pane pane = loader.load();

            SellerFormController controller = loader.getController();
            controller.setSeller(obj);
            controller.setServices(new SellerService(), new DepartmentService());
            controller.loadAssociatedObjects();
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

    private void deleteSeller(Seller obj) {
        Optional<ButtonType> result = Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
        if (result.get() == ButtonType.OK) {
            if (sellerService == null) {
                throw new IllegalStateException("Service is null");
            }
            try {
                sellerService.deleteSeller(obj);
                updateTableView();
            } catch (DbIntegrityException e) {
                Alerts.showAlert("Error removing object", null, e.getMessage(), Alert.AlertType.ERROR);
            }

        }
    }

    private void initEditButtons() {
        tableColumnEdit.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnEdit.setCellFactory(param ->
                new TableCell<Seller, Seller>() {

                    private final Button btnEdit = new Button();


                    @Override
                    protected void updateItem(Seller obj, boolean empty) {
                        super.updateItem(obj, empty);

                        File imgEdit = new File("src/main/resources/logo/edit.png");
                        ImageView view = new ImageView(imgEdit.toURI().toString());
                        btnEdit.setGraphic(view);

                        if (obj == null) {
                            setGraphic(null);
                            return;
                        }

                        setGraphic(btnEdit);
                        btnEdit.setOnAction(event -> loadDialogForm(obj, "SellerForm.fxml", Utils.currentStage(event)));
                    }
                });
    }

    private void initRemoveButtons() {
        tableColumnRemove.setCellValueFactory(param ->
                new ReadOnlyObjectWrapper<>(param.getValue()));
        tableColumnRemove.setCellFactory(param ->
                new TableCell<Seller, Seller>() {

                    private final Button btnRemove = new Button();

                    @Override
                    public void updateItem(Seller obj, boolean empty) {
                        super.updateItem(obj, empty);

                        File imgRemove = new File("src/main/resources/logo/remove-x.png");
                        ImageView viewRemove = new ImageView(imgRemove.toURI().toString());
                        btnRemove.setGraphic(viewRemove);

                        if (obj == null) {
                            setGraphic(null);
                            return;
                        }

                        setGraphic(btnRemove);
                        btnRemove.setOnAction(event -> deleteSeller(obj));
                    }
                });
    }

    @Override
    public void onDataChanged() {
        updateTableView();
    }
}
