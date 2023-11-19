package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

public class LoginController {
    @FXML
    private TextField usernameField;

    private List<String> validUsers = Arrays.asList("user1", "user2", "admin"); // Example users

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        if (validUsers.contains(username)) {
            // Proceed to main application
            // For now, just show a success message
            showAlert("Login Successful", "Welcome, " + username + "!");
        } else {
            // Show error message
            showAlert("Login Failed", "Invalid username.");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
