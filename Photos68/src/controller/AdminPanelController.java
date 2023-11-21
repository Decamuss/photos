package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.User;
import utils.DataManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button;

/**
 * Controller class for the admin panel.
 * This class handles the user interface interactions for managing users in the application.
 */
public class AdminPanelController {

    @FXML
    private ListView<String> userList;

    @FXML
    private TextField newUserField;

    @FXML
    private Button logoutButton;

    /**
     * Initializes the controller class.
     * This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        updateListView(); // Call updateListView when initializing
    }

    /**
     * Updates the ListView with the current list of users.
     * This method fetches users from the data manager and updates the user interface.
     */
    private void updateListView() {
        List<User> users;
        try {
            users = DataManager.loadUsers();
        } catch (Exception e) {
            e.printStackTrace();
            users = new ArrayList<>(); // Set to an empty list in case of an error
        }

        userList.getItems().clear();
        for (User user : users) {
            userList.getItems().add(user.getUsername());
        }
    }

    /**
     * Shows an error message in a dialog box.
     *
     * @param message The error message to be displayed.
     */
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Handles the action of adding a new user.
     * This method is called when the add user button is clicked.
     */
    @FXML
    private void handleAddUser() {
        String username = newUserField.getText().trim(); // Trim to remove any leading/trailing spaces

        // Check for reserved usernames
        if ("admin".equalsIgnoreCase(username)) {
            showError("The username '" + username + "' is not allowed.");
            return; // Return to prevent further processing
        }

        // Check for blank username
        if (username.isEmpty()) {
            showError("Username cannot be blank.");
            return; // Return to prevent further processing
        }

        // Check for duplicate username
        try {
            List<User> users = DataManager.loadUsers();
            for (User user : users) {
                if (user.getUsername().equalsIgnoreCase(username)) {
                    showError("The username '" + username + "' is already taken.");
                    return; // Return to prevent further processing
                }
            }

            // If username is unique, add the user
            DataManager.addUser(new User(username));
            updateListView(); // Update the ListView after adding a user
            newUserField.clear();
        } catch (Exception e) {
            e.printStackTrace();
            // Handle exceptions here, maybe show an error message to the user
        }
    }

    /**
     * Handles the action of deleting a selected user.
     * This method is called when the delete user button is clicked.
     */
    @FXML
    private void handleDeleteUser() {
        String selectedUser = userList.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            try {
                DataManager.deleteUser(selectedUser);
                updateListView(); // Update the ListView after deleting a user
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                // Handle exceptions here
            }
        }
        // Add error handling if needed
    }

    /**
     * Handles the logout action.
     * This method is called when the logout button is clicked.
     * It closes the admin panel and optionally opens the login screen.
     */
    @FXML
    private void handleLogout() {
        // Close the current stage (admin panel)
        Stage stage = (Stage) logoutButton.getScene().getWindow();
        stage.close();

        // Optionally, open the login window again
        showLoginScreen();
    }

    /**
     * Shows the login screen.
     * This method loads and displays the login window.
     */
    private void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Stage loginStage = new Stage();
            loginStage.setScene(new Scene(loader.load(), 300, 275));

            loginStage.setTitle("Photo Application");
            loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception, maybe show an error message
        }
    }

    // Add more methods as needed
}
