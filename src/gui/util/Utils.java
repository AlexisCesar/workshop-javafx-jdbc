package gui.util;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;

public class Utils {

	public static Stage currentStage(ActionEvent evt) {
		return (Stage) ((Node) evt.getSource()).getScene().getWindow();
	}
	
}
