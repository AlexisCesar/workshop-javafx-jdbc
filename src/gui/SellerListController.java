package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbIntegrityException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Seller;
import model.services.SellerService;

public class SellerListController implements Initializable, DataChangeListener {

	private SellerService service;

	@FXML
	private TableView<Seller> tblViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tblColumnID;

	@FXML
	private TableColumn<Seller, String> tblColumnName;
	
	@FXML
	private TableColumn<Seller, String> tblColumnEmail;

	@FXML
	private TableColumn<Seller, Date> tblColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tblColumnBaseSalary;

	@FXML
	private TableColumn<Seller, Seller> tblColumnEDIT;

	@FXML
	private TableColumn<Seller, Seller> tblColumnREMOVE;

	@FXML
	private Button btnNew;

	private ObservableList<Seller> obsList;

	@FXML
	public void onBtnNewAction(ActionEvent evt) {
		Stage parentStage = Utils.currentStage(evt);
		Seller obj = new Seller();
		createDialogForm(obj, "/gui/SellerForm.fxml", parentStage);
	}

	public void setSellerService(SellerService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	private void initializeNodes() {
		tblColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		tblColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tblColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tblColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tblColumnBirthDate, "dd/MM/yyyy");
		tblColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tblColumnBaseSalary, 2);

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tblViewSeller.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException("Service was null!");
		}

		List<Seller> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tblViewSeller.setItems(obsList);
		initEditButtons();
		initRemoveButtons();
	}

	private void createDialogForm(Seller obj, String absoluteName, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteName));
			Pane pane = loader.load();

			SellerFormController controller = loader.getController();
			controller.setSeller(obj);
			controller.setSellerService(new SellerService());
			controller.subscribeDataChangeListener(this);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Enter seller data");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void onDataChanged() {

		updateTableView();

	}

	private void initEditButtons() {
		tblColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tblColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(
						event -> createDialogForm(obj, "/gui/SellerForm.fxml", Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tblColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tblColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	private void removeEntity(Seller seller) {
		Optional<ButtonType> result =  Alerts.showConfirmation("Confirmation", "Are you sure to delete?");
		
		if(result.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			try {
				service.remove(seller);
				updateTableView();
			} catch(DbIntegrityException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
		
	}

}
