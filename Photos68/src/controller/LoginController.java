package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Album;
import model.User;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.List;
import utils.DataManager; // Import DataManager

public class LoginController {
    @FXML
    private TextField usernameField;

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText().trim(); // Trim to remove any leading/trailing spaces

        try {
            List<User> users = DataManager.loadUsers(); // Load users dynamically
            boolean isValidUser = users.stream().anyMatch(user -> user.getUsername().equals(username));

            if ("admin".equals(username)) {
                    // Launch admin panel
            loadScene("/view/AdminPanel.fxml");
            } else if (isValidUser) {

                    // Handle regular user login
                    User.currentUser = new User(username);
                    //User.currentUser.addAlbums(new Album("test"));
                    loadScene("/view/AlbumsListPage.fxml");
                }
             else {
                // Show error message
                showAlert("Login Failed", "Invalid username.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load user data.");
        }
    }

    private void loadScene(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
        Stage stage = (Stage) usernameField.getScene().getWindow();
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.show();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
