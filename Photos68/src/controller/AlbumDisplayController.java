package controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javafx.scene.control.TableCell;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Album;
import model.Photo;
import model.User;
import utils.DataManager;
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
    private Button SaveButton;

    @FXML
    private int currentPhotoIndex = 0;


    @FXML
    void AddPhotoRequest(ActionEvent event) {
        Stage stage = (Stage) ((Button) AddPhotoButton).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image File");
        fileChooser.getExtensionFilters().addAll(
        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif", "*.bmp", "*.jpeg"));
        selectedFile = fileChooser.showOpenDialog(stage);
       
        // Check for duplicate photos
        boolean isDuplicate = false;
        for (Photo photo : Album.currentAlbum.getPhotos()) {
            if (selectedFile.equals(photo.getFile())) {
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
        } else {
            // If no duplicate is found, proceed to add the new photo
            Photo newPhoto = new Photo(selectedFile);
            Album.currentAlbum.addPhoto(newPhoto);
            newPhoto.setCaption("work");
            realPhotoList.getItems().add(newPhoto);
        }
        
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
        Photo photoToRemove = Photo.tempMove;
        Album.currentAlbum.removePhoto(photoToRemove);
        Album.currentAlbum = null;
    
        photoList.remove(photoToRemove);
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
            showAlert("Success", "Data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save data.");
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AlbumsListPage.fxml"));
            Stage stage = (Stage) BackButton.getScene().getWindow();
            Scene scene = new Scene(loader.load());
            stage.setScene(scene);
            stage.show();
        } catch(IOException e) {
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
    @FXML
    void SaveRequest(ActionEvent event) {
        String albumName = Album.currentAlbum.getName().trim();
    
        // Check for blank name
        if (albumName.isEmpty()) {
            showAlert("Error", "Album name cannot be blank.");
            return;
        }
    
        // Check for duplicate name
        boolean isDuplicate = User.currentUser.getAlbums().stream()
            .anyMatch(album -> !album.equals(Album.currentAlbum) && album.getName().equalsIgnoreCase(albumName));
    
        if (isDuplicate) {
            showAlert("Error", "An album with this name already exists.");
            return;
        }
    
        // Update current user's album list if necessary
        if (!User.currentUser.getAlbums().contains(Album.currentAlbum)) {
            User.currentUser.getAlbums().add(Album.currentAlbum);
        }
    
        // Proceed with saving
        try {
            // Load all users
            List<User> allUsers = DataManager.loadUsers();
    
            // Update the current user in the list
            for (int i = 0; i < allUsers.size(); i++) {
                if (allUsers.get(i).getUsername().equals(User.currentUser.getUsername())) {
                    allUsers.set(i, User.currentUser);
                    break;
                }
            }
    
            // Save all users
            DataManager.saveUsers(allUsers);
            showAlert("Success", "Data saved successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save data.");
        }
    }
    


    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
            Album newAlbum = new Album("");
            Album.currentAlbum = newAlbum;
            User.currentUser.addAlbums(newAlbum);
        }

        Platform.runLater(() -> {
            // Get the current stage from one of the components
            Stage stage = (Stage) SaveButton.getScene().getWindow();

            // Set the close request handler
            stage.setOnCloseRequest(event -> {
                // Call the save method
                SaveRequest(new ActionEvent());

                // Close the stage
                //stage.close();
            });
        });
    }
    
    @FXML
    public void startSlideshow() {
        if (realPhotoList.getItems().isEmpty()) {
            // Handle empty album case
            return;
        }

        Stage slideshowStage = new Stage();
        slideshowStage.initModality(Modality.APPLICATION_MODAL);

        VBox vbox = new VBox(10);
        ImageView imageView = new ImageView();
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(300); // Adjust size as needed
        imageView.setFitWidth(300);  // Adjust size as needed

        Button nextButton = new Button("Next");
        Button prevButton = new Button("Previous");
        Label captionLabel = new Label();

        nextButton.setOnAction(e -> showNextPhoto(imageView, captionLabel));
        prevButton.setOnAction(e -> showPreviousPhoto(imageView, captionLabel));

        vbox.getChildren().addAll(imageView, captionLabel, prevButton, nextButton);

        Scene scene = new Scene(vbox, 400, 400); // Adjust scene size as needed
        slideshowStage.setScene(scene);
        slideshowStage.show();

        showPhotoAtIndex(imageView, captionLabel, currentPhotoIndex);
    }

    private void showNextPhoto(ImageView imageView, Label captionLabel) {
        if (currentPhotoIndex < realPhotoList.getItems().size() - 1) {
            currentPhotoIndex++;
            showPhotoAtIndex(imageView, captionLabel, currentPhotoIndex);
        }
    }

    private void showPreviousPhoto(ImageView imageView, Label captionLabel) {
        if (currentPhotoIndex > 0) {
            currentPhotoIndex--;
            showPhotoAtIndex(imageView, captionLabel, currentPhotoIndex);
        }
    }

    private void showPhotoAtIndex(ImageView imageView, Label captionLabel, int index) {
        Photo photo = realPhotoList.getItems().get(index);
        Image image = new Image(photo.getFile().toURI().toString());
        imageView.setImage(image);
        captionLabel.setText(photo.getCaption());
    }
}


