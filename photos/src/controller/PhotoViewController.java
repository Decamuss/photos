package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
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
    private TableColumn<Map.Entry<String,String>, String> TagName;

    @FXML
    private TableView<Map.Entry<String,String>> TagTable;

    @FXML
    private TableColumn<Map.Entry<String,String>,String> TagValue;

    private Image ImageToView;

    ObservableList<Map.Entry<String,String>> tagList = FXCollections.observableArrayList();
    @Override
     public void initialize(URL location, ResourceBundle bundle) {
        // Load the image from a file
        

        ImageToView = new Image(Photo.currentPhoto.getFile().toURI().toString()); // Replace with the actual path to your image file

        // Set the image to the ImageView
        PhotoPreview.setImage(ImageToView);

        Caption.setText(Photo.currentPhoto.getCaption());
        Date.setText("" + Photo.currentPhoto.getDateTaken());

      
        if(!Photo.currentPhoto.getTags().isEmpty())
        {
        
        tagList.addAll(Photo.currentPhoto.getTags().entrySet());

        TagName.setCellValueFactory(cellData -> {
        Map.Entry<String, String> entry = (Map.Entry<String, String>) cellData.getValue();
        return new ReadOnlyStringWrapper(entry.getKey());
    });

    TagValue.setCellValueFactory(cellData -> {
        Map.Entry<String, String> entry = (Map.Entry<String, String>) cellData.getValue();
        return new ReadOnlyStringWrapper(entry.getValue());
    });

        TagTable.getItems().setAll(tagList);
        }
    }
    


    @FXML
    void AddTagRequest(ActionEvent event) {
            // Create and configure the dialog
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Add Tag");
        dialog.setHeaderText("Enter a Tag Name:Tag value");

        // Set the button types
        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        // Create a text field for album name input
        TextField tagField = new TextField();
        tagField.setPromptText("Tag");

        // Set the dialog content
        dialog.getDialogPane().setContent(tagField);

        // Enable/Disable OK button depending on whether the album name was entered.
        Node okButton = dialog.getDialogPane().lookupButton(okButtonType);
        okButton.setDisable(true);
        
                // Do some validation (using the Java 8 lambda syntax).
        tagField.textProperty().addListener((observable, oldValue, newValue) -> {
            okButton.setDisable(newValue.trim().isEmpty());
        });

        // Convert the result to a string when OK button is clicked
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                return tagField.getText().trim();
            }
            return null;
        });

        // Show the dialog and wait for the user response
        Optional<String> result = dialog.showAndWait();
       
        
            if (result.isPresent()) {
                if (isValidTag(result.get())) {
                    String[] tagEquals = result.get().split(":");
                    if (tagEquals.length == 2) {
                        Photo.currentPhoto.addTag(tagEquals[0], tagEquals[1]);
                    }



                } else {
                    // Show an invalid prompt to the user
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Tag Format");
                    alert.setHeaderText(null);
                    alert.setContentText("Please enter the tag in the format 'string:string'.");
                    alert.showAndWait();
                }
            }
    
    }

            // Validate the tag format
        private boolean isValidTag(String tag) {
            // The tag should be in the format "string:string"
            return tag.matches(".+:.+");
        }

    @FXML
    void PVSaveRequest(ActionEvent event) {

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
