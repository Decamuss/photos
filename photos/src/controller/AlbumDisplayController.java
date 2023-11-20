package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import javafx.scene.control.TableCell;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Album;
import model.Photo;
import model.User;
import javafx.fxml.Initializable;


public class AlbumDisplayController implements Initializable{
     ObservableList<Photo> photoList = FXCollections.observableArrayList();
    @FXML
    private TableView<Photo> realPhotoList;
    
    private File selectedFile;

    @FXML
    private Button AddPhotoButton;

    @FXML
    private TextField AlbumName;

    @FXML
    private TableColumn<Photo, String> CaptionPhoto;

    @FXML
    private Button CopyButton;

    @FXML
    private Button EditPhotoButton;

    @FXML
    private Button MoveButton;

    @FXML
    private Button RemovePhotoButton;

    @FXML
    private Button BackButton;

    @FXML
    private TableColumn<Photo, File> ThumbnailPhoto;

    @FXML
    void AddPhotoRequest(ActionEvent event) {
        Stage stage = (Stage) ((Button) AddPhotoButton).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg"));
        selectedFile = fileChooser.showOpenDialog(stage);
        Photo newPhoto = new Photo(selectedFile);
        Album.currentAlbum.addPhoto(newPhoto);
        newPhoto.setCaption("work");
        realPhotoList.getItems().add(newPhoto);
    
    
    }

    @FXML
    void AlbumRenameRequest(ActionEvent event) {
        if(Album.currentAlbum !=null)
        {
            String newName = AlbumName.getText();
            Album.currentAlbum.setName(newName);
        }
    }

    @FXML
    void CopyRequest(ActionEvent event) {
    Photo.tempCopy = realPhotoList.getSelectionModel().getSelectedItem();
    Album.currentAlbum = null;
     try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumsListPage.fxml"));
            Stage stage = (Stage) BackButton.getScene().getWindow();
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
    void EditPhotoRequest(ActionEvent event) {
        Photo.currentPhoto = realPhotoList.getSelectionModel().getSelectedItem();
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/PhotoView.fxml"));
            Stage stage = (Stage) EditPhotoButton.getScene().getWindow();
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
    void MoveRequest(ActionEvent event) {
    Photo.tempMove = realPhotoList.getSelectionModel().getSelectedItem();
    Photo photoToRemove =realPhotoList.getSelectionModel().getSelectedItem();
    Album.currentAlbum.removePhoto(photoToRemove);
    Album.currentAlbum = null;
    photoList.remove(photoToRemove);
     try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumsListPage.fxml"));
            Stage stage = (Stage) BackButton.getScene().getWindow();
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
    void RemovePhotoRequest(ActionEvent event) {
    Photo photoToRemove =realPhotoList.getSelectionModel().getSelectedItem();
    Album.currentAlbum.removePhoto(photoToRemove);
    photoList.remove(photoToRemove);
    }

    @FXML
    void ReturnRequest(ActionEvent event) {
        Album.currentAlbum = null;
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumsListPage.fxml"));
            Stage stage = (Stage) BackButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        }
        catch(IOException e)
        {
             e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if(Album.currentAlbum != null) 
        {
            photoList.addAll(Album.currentAlbum.getPhotos());
            CaptionPhoto.setCellValueFactory(new PropertyValueFactory<Photo, String>("caption"));
            ThumbnailPhoto.setCellValueFactory(new PropertyValueFactory<Photo, File>("file"));
            ThumbnailPhoto.setCellFactory(column -> {
            return new TableCell<Photo, File>() {
                private final ImageView imageView = new ImageView();

                @Override
                protected void updateItem(File file, boolean empty) {
                    super.updateItem(file, empty);
                    if (file == null || empty) {
                        setGraphic(null);
                    } else {
                        Image image = new Image(file.toURI().toString());
                        imageView.setImage(image);
                        imageView.setFitWidth(150); // set the width and height of the image view
                        imageView.setFitHeight(150);
                        setGraphic(imageView);
                    }
                }
            };
        });
        realPhotoList.setItems(photoList);
            AlbumName.setText(Album.currentAlbum.getName());
        }
        
          if(Album.currentAlbum == null) 
        {
            Album newAlbum = new Album(null);
            Album.currentAlbum = newAlbum;
            User.currentUser.addAlbums(newAlbum);
        }
    }

}
