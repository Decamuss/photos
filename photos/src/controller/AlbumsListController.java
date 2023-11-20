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
import javafx.fxml.FXMLLoader;
import java.io.IOException;
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

   

    @Override
    public void initialize(URL location, ResourceBundle bundle)
    {
        albumList.addAll(User.currentUser.getAlbums());
        realAlbumList.setItems(albumList);
         realAlbumList.getSelectionModel().selectedIndexProperty().addListener( (obs, oldVal, newVal) -> {
        if (newVal.intValue() >= 0) { // Make sure an item is selected
            Album selectedAlbum = realAlbumList.getItems().get(newVal.intValue());
            if(Photo.tempMove != null)
            {
                selectedAlbum.addPhoto(Photo.tempMove);
                Photo.tempMove = null;
            }
            else if(Photo.tempCopy != null)
            {
                selectedAlbum.addPhoto(Photo.tempCopy);
                Photo.tempCopy = null;
            }
        }
    });
    }


    @FXML
    void AddRequest(ActionEvent event) {
        User.currentUser.addAlbums(new Album("new"));
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

}
