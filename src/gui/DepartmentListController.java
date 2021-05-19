package gui;

import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;

public class DepartmentListController implements Initializable {

	@FXML
	private TableView<Department> tblViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tblColumnID;
	
	@FXML
	private TableColumn<Department, String> tblColumnName;
	
	@FXML
	private Button btnNew;
	
	@FXML
	public void onBtnNewAction() {
		System.out.println("onBtnNewAction");
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		
		initializeNodes();
		
	}

	private void initializeNodes() {
		tblColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
		tblColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tblViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
	}

}
