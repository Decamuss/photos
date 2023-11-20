package controller;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Album;
import model.User;
import utils.DataManager;
import javafx.fxml.FXMLLoader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import model.Photo;
import javafx.scene.Node;



public class AlbumsListController implements Initializable {
    ObservableList<Album> albumList = FXCollections.observableArrayList();
    @FXML
    Button LogoutButton;
    @FXML
    Button EditButton;
    @FXML
    private ListView<Album> realAlbumList;

    @FXML
    private Button SaveButton;

    @FXML
    private Button SortButton;


    @Override
    public void initialize(URL location, ResourceBundle bundle) {
        try {
            // Load users and find the current user
            List<User> users = DataManager.loadUsers();
            User currentUser = findCurrentUser(users);

            if (currentUser != null) {
                // Update the current user's albums from the loaded data
                User.currentUser.setAlbums(currentUser.getAlbums());
                albumList.addAll(User.currentUser.getAlbums()); // Load albums from the current user
            } else {
                showAlert("Error", "Current user not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to load albums.");
        }
        realAlbumList.setItems(albumList);
        // realAlbumList.getSelectionModel().selectedIndexProperty().addListener((obs, oldVal, newVal) -> {
        //     if (newVal.intValue() >= 0) { // Make sure an item is selected
        //         Album selectedAlbum = realAlbumList.getItems().get(newVal.intValue());
        //           if (Photo.tempMove != null) {
        //             selectedAlbum.addPhoto(Photo.tempMove);
        //             Photo.tempMove = null;
        //         } else if (Photo.tempCopy != null) {
        //             selectedAlbum.addPhoto(Photo.tempCopy);
        //             Photo.tempCopy = null;
        //         }
        //     }      
        // });

    }


    @FXML
    void AddRequest(ActionEvent event) {
        // Create and configure the dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Album");
        dialog.setHeaderText("Enter a Name for the New Album");

        // Set the button types
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Create a text field for album name input
        TextField albumNameField = new TextField();
        albumNameField.setPromptText("Album Name");

        // Set the dialog content
        dialog.getDialogPane().setContent(albumNameField);

        // Enable/Disable OK button depending on whether the album name was entered.
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);

        // Do some validation (using the Java 8 lambda syntax).
        albumNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        // Convert the result to a string when OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return albumNameField.getText().trim();
            }
            return null;
        });

        // Show the dialog and wait for the user response
        Optional<String> result = dialog.showAndWait();

        // Handle the result
        result.ifPresent(albumName -> {
            if (isDuplicateAlbumName(albumName)) {
                showAlert("Error", "Album name already exists.");
            } else {
                Album newAlbum = new Album(albumName);
                User.currentUser.addAlbums(newAlbum);
                albumList.add(newAlbum);
                try {
                    DataManager.saveUsers(Arrays.asList(User.currentUser));
                    showAlert("Success", "Album added successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                    showAlert("Error", "Failed to add album.");
                }
            }
        });
    }

private boolean isDuplicateAlbumName(String albumName) {
    return User.currentUser.getAlbums().stream()
           .anyMatch(album -> album.getName().equalsIgnoreCase(albumName));
}
    

    @FXML
    void DeleteRequest(ActionEvent event) {
        Album itemToRemove = realAlbumList.getSelectionModel().getSelectedItem();
        albumList.remove(itemToRemove);
        User.currentUser.removeAlbum(itemToRemove);
    }

    @FXML
    void EditRequest(ActionEvent event) {
        Album.currentAlbum = realAlbumList.getSelectionModel().getSelectedItem();
        if(Photo.tempMove != null)
        {
            Album.currentAlbum.addPhoto(Photo.tempMove);
            Photo.tempMove = null;
        }
        if(Photo.tempCopy != null)
        {
            Album.currentAlbum.addPhoto(Photo.tempCopy);
            Photo.tempCopy =null;
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumDisplay.fxml"));
            Stage stage = (Stage) EditButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e)
        {
             e.printStackTrace();
        }
    }

    @FXML
    void LogoutRequest(ActionEvent event) {
        
        User.currentUser = null;
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Stage stage = (Stage) LogoutButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e)
        {
             e.printStackTrace();
        }
    }

    @FXML
    void SaveRequest(ActionEvent event) {
        try {
            List<User> users = DataManager.loadUsers();
            User currentUser = findCurrentUser(users);

            if (currentUser != null) {
                currentUser.setAlbums(new ArrayList<>(albumList)); // Update the current user's albums
                DataManager.saveUsers(users); // Save the updated list of users
                showAlert("Success", "Albums saved successfully.");
            } else {
                showAlert("Error", "Current user not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save albums.");
        }
    }

    private User findCurrentUser(List<User> users) {
        return users.stream()
                    .filter(u -> u.getUsername().equals(User.currentUser.getUsername()))
                    .findFirst()
                    .orElse(null);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    void SortRquest(ActionEvent event) {

    }

}
