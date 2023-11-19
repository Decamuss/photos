package controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import model.User;
import utils.DataManager;

import java.io.IOException;
import java.util.List;

public class AdminController {
    @FXML
    private ListView<String> userList;

    private List<User> users;

    public void initialize() {
        try {
            users = DataManager.loadUsers(); // Adjusted to match DataManager
            updateListView();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            // Handle exceptions
        }
    }

    private void updateListView() {
        userList.getItems().clear();
        for (User user : users) {
            userList.getItems().add(user.getUsername());
        }
    }

    // Add methods to handle adding, deleting users, etc.
}
