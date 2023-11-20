package controller;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;
import utils.DataManager;

public class PhotoViewController implements Initializable {

    @FXML
    private Button AddTagButton;

    @FXML
    private TextField Caption;

    @FXML
    private Label Date;

    @FXML
    private Button PVSaveButton;

    @FXML
    private ImageView PhotoPreview;

    @FXML
    private Button RemoveTagButton;

    @FXML
    private Button ReturnButton;

    @FXML
    private TableColumn<?, ?> TagName;

    @FXML
    private TableView<?> TagTable;

    @FXML
    private TableColumn<?, ?> TagValue;

    private Image ImageToView;
    @Override
     public void initialize(URL location, ResourceBundle bundle) {
        // Load the image from a file
        
        ImageToView = new Image(Photo.currentPhoto.getFile().toURI().toString()); // Replace with the actual path to your image file

        // Set the image to the ImageView
        PhotoPreview.setImage(ImageToView);

        Caption.setText(Photo.currentPhoto.getCaption());
        Date.setText("" + Photo.currentPhoto.getDateTaken());
    }


    @FXML
    void AddTagRequest(ActionEvent event) {

    }

    @FXML
    void PVSaveRequest(ActionEvent event) {
                // Proceed with saving
        try {
            DataManager.saveUsers(Arrays.asList(User.currentUser)); // Assuming User.currentUser contains the current user's data
            showAlert("Success", "Data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save data.");
        }
    }

    @FXML
    void PreviousScreenRequest(ActionEvent event) {
            Photo.currentPhoto = null;
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumDisplay.fxml"));
            Stage stage = (Stage) ReturnButton.getScene().getWindow();
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
    void RenameCaption(ActionEvent event) {
        if(Photo.currentPhoto !=null)
        {
            String newName = Caption.getText();
            Photo.currentPhoto.setCaption(newName);
        }
    }

    @FXML
    void RequestRemoveTag(ActionEvent event) {

    }

    private void showAlert(String title, String message) {
    Alert alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(null);
    alert.setContentText(message);
    alert.showAndWait();
}

}
