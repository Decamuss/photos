package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import model.User;
import utils.DataManager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class AdminPanelController {

    @FXML
    private ListView<String> userList;

    @FXML
    private TextField newUserField;

    private List<User> users; // List to hold users

    // Initialize method
    @FXML
    public void initialize() {
        updateListView(); // Call updateListView when initializing
    }

    // Method to update the ListView with current users
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


    // Method to handle adding a new user
    @FXML
    private void handleAddUser() {
        String username = newUserField.getText();
        if (!username.isEmpty()) {
            try {
                DataManager.addUser(new User(username));
                updateListView(); // Update the ListView after adding a user
                newUserField.clear();
            } catch (Exception e) { // Catch a general exception if you're unsure about the specific type
                e.printStackTrace();
                // Handle exceptions here, maybe show an error message to the user
            }
        }
        // Add error handling if needed
    }
    

    // Method to handle deleting a user
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

    // Add more methods as needed
}
