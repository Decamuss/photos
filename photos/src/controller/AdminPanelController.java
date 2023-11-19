package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import model.User;

import java.util.ArrayList;
import java.util.List;

public class AdminPanelController {

    @FXML
    private ListView<String> userList;

    @FXML
    private Button addUserButton;

    @FXML
    private Button deleteUserButton;

    @FXML
    private TextField newUserField;

    private List<User> users; // This will be your model holding the list of users

    public AdminPanelController() {
        this.users = new ArrayList<>();
    }
    // Initialize method
    @FXML
    public void initialize() {
        // Initialize your user list here
        // For example, load users from a file or a database
        // Then update the userList view
        updateListView();
    }

    // Method to update the ListView with current users
    private void updateListView() {
        userList.getItems().clear();
        for (User user : users) {
            userList.getItems().add(user.getUsername());
        }
    }

    // Method to handle adding a new user
    @FXML
    private void handleAddUser() {
        String username = newUserField.getText();
        if (!username.isEmpty()) {
            // Add user to your model
            users.add(new User(username));
            updateListView();
            newUserField.clear();
        }
        // Add error handling if needed
    }

    // Method to handle deleting a user
    @FXML
    private void handleDeleteUser() {
        String selectedUser = userList.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            // Remove user from your model
            users.removeIf(user -> user.getUsername().equals(selectedUser));
            updateListView();
        }
        // Add error handling if needed
    }

    // Add more methods as needed
}
