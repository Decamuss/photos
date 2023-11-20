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
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import java.util.ResourceBundle;
import java.net.URL;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import model.Photo;


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

    }


    @FXML
    void AddRequest(ActionEvent event) {
        //User.currentUser.addAlbums(new Album("new"));
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
    void DeleteRequest(ActionEvent event) {
        Album itemToRemove = realAlbumList.getSelectionModel().getSelectedItem();
        albumList.remove(itemToRemove);
        User.currentUser.removeAlbum(itemToRemove);
    }

    @FXML
    void EditRequest(ActionEvent event) {
        Album.currentAlbum = realAlbumList.getSelectionModel().getSelectedItem();
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
