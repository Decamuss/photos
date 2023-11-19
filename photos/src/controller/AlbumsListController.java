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

public class AlbumsListController implements Initializable {
    ObservableList<Album> albumList = FXCollections.observableArrayList();
    @FXML
    Button LogoutButton;
    @FXML
    private ListView<Album> realAlbumList;
    @Override
    public void initialize(URL location, ResourceBundle bundle)
    {
        albumList.addAll(User.currentUser.getAlbums());
        realAlbumList.setItems(albumList);
    }


    @FXML
    void AddRequest(ActionEvent event) {

    }

    @FXML
    void DeleteRequest(ActionEvent event) {
        Album itemToRemove = realAlbumList.getSelectionModel().getSelectedItem();
        albumList.remove(itemToRemove);
        User.currentUser.removeAlbum(itemToRemove);
    }

    @FXML
    void EditRequest(ActionEvent event) {
        
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
