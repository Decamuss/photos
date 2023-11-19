package controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import model.Album;

public class LoginController {
    @FXML
    private TextField usernameField;

    private List<String> validUsers = Arrays.asList("user1", "user2", "admin","user3" ); // Example users

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        if (validUsers.contains(username)) {
            if ("admin".equals(username)) {
                // Launch admin panel
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AdminPanel.fxml"));
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to load admin panel.");
                }
            } else {
                // Handle regular user login
                // Here you would typically load the main application scene for regular users
                
                User.currentUser = new User(username);
                User.currentUser.addAlbums(new Album("test"));
                try{
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumsListPage.fxml"));
                    Stage stage = (Stage) usernameField.getScene().getWindow();
                    Scene scene = new Scene(loader.load());
                    stage.setScene(scene);
                    stage.show();
                }
                catch(IOException e)
                {
                     e.printStackTrace();
                    showAlert("Error", "Failed to load user panel.");
                }
            }
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
