package controller;

import javafx.application.Platform;
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

import java.io.File;
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
    private Button SearchButton;

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
        Platform.runLater(() -> {
            // Get the current stage from one of the components
            Stage stage = (Stage) LogoutButton.getScene().getWindow();

            // Set the close request handler
            stage.setOnCloseRequest(event -> {
                // Call the save method
                SaveRequest(new ActionEvent());

                // Close the stage
                //event.consume(); // Consume the event if you don't want it to proceed with the default close operation
            });
        });
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
                try {
                    List<User> users = DataManager.loadUsers(); // Load all users
        
                    // Find the current user in the list
                    Optional<User> existingUser = users.stream()
                                                       .filter(u -> u.getUsername().equals(User.currentUser.getUsername()))
                                                       .findFirst();
        
                    Album newAlbum = new Album(albumName);
                    if (existingUser.isPresent()) {
                        // Update the current user's albums
                        existingUser.get().addAlbums(newAlbum);
                        User.currentUser.addAlbums(newAlbum); // Update the in-memory user data
                    } else {
                        // If the current user is not in the list, add them
                        User.currentUser.addAlbums(newAlbum);
                        users.add(User.currentUser);
                    }
        
                    DataManager.saveUsers(users); // Save the updated list of users
                    refreshAlbumList(); // Refresh the album list
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

        try {
            // Load all users
            List<User> users = DataManager.loadUsers();
            // Find and update the current user in the list
            for (User user : users) {
                if (user.getUsername().equals(User.currentUser.getUsername())) {
                    user.setAlbums(User.currentUser.getAlbums());
                    break;
                }
            }
            // Save all users
            DataManager.saveUsers(users);
            showAlert("Success", "Album deleted successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to delete album.");
        }
    }


    
    public void refreshAlbumList() {
        // Clear the current observable list
        albumList.clear();

        // Add all albums of the current user to the observable list
        albumList.addAll(User.currentUser.getAlbums());
    }


    @FXML
    void EditRequest(ActionEvent event) {
        Album.currentAlbum = realAlbumList.getSelectionModel().getSelectedItem();
        if(Photo.tempMove != null)
        {   
            boolean isDuplicate =false;
            File tempMoveFile = Photo.tempMove.getFile();
            for (Photo photo : Album.currentAlbum.getPhotos()) {
                if (tempMoveFile.equals(photo.getFile())) {
                    isDuplicate = true;
                    break;  // Exit the loop as soon as a duplicate is found
                }
        }
            
            if (isDuplicate) {
                // Show an error alert for duplicate photo
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Photo Cannot be Added");
                alert.setHeaderText(null);
                alert.setContentText("Please select a different photo.");
                alert.showAndWait();
                return;
            } else {
                // If no duplicate is found, proceed to add the new photo
                Album.currentAlbum.addPhoto(new Photo(tempMoveFile));
                Photo.tempMove = null;
            }
        }

        if(Photo.tempCopy != null)
        {
            boolean isDuplicate =false;
            File tempCopyFile = Photo.tempCopy.getFile();
            for (Photo photo : Album.currentAlbum.getPhotos()) {
                if (tempCopyFile.equals(photo.getFile())) {
                    isDuplicate = true;
                    break;  // Exit the loop as soon as a duplicate is found
                }
        }
            
            if (isDuplicate) {
                // Show an error alert for duplicate photo
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Duplicate Photo Cannot be Added");
                alert.setHeaderText(null);
                alert.setContentText("Please select a different photo.");
                alert.showAndWait();
                return;
            } else {
                // If no duplicate is found, proceed to add the new photo
                Album.currentAlbum.addPhoto(new Photo(tempCopyFile));
                Photo.tempCopy = null;
            }
        }
        
        if(Album.currentAlbum!=null)
        {
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
    }

    @FXML
    void LogoutRequest(ActionEvent event) {
        
        User.currentUser = null;
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Stage stage = (Stage) LogoutButton.getScene().getWindow();
            Scene scene = new Scene(loader.load(), 300, 275);
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
            List<User> users = DataManager.loadUsers(); // Load all users
    
            // Find the current user in the list
            Optional<User> existingUser = users.stream()
                                               .filter(u -> u.getUsername().equals(User.currentUser.getUsername()))
                                               .findFirst();
    
            if (existingUser.isPresent()) {
                // Update the current user's albums
                existingUser.get().setAlbums(new ArrayList<>(albumList));
            } else {
                // If the current user is not in the list, add them
                User.currentUser.setAlbums(new ArrayList<>(albumList));
                users.add(User.currentUser);
            }
    
            DataManager.saveUsers(users); // Save the updated list of users
            showAlert("Success", "Albums saved successfully.");
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
    void SearchRequest(ActionEvent event) {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/SearchPanel.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load())); // Load the FXML file
            stage.setTitle("Search Photos");
    
            // Now get the controller
            SearchPanelController searchController = loader.getController();
            if (searchController != null) {
                searchController.setAlbumsListController(this); // Pass the reference
            } else {
                // Handle the case where the controller didn't load properly
                System.err.println("SearchPanelController is null.");
                return;
            }
    
            // Show the stage
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // Handle the exception
        }
    }
    
    // In AlbumsListController
    public void addNewAlbum(Album album) {
        albumList.add(album);
        User.currentUser.addAlbums(album);
        // Save the user data if necessary
    }

}
