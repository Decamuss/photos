import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;



public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        // Handle login logic
    }
}
