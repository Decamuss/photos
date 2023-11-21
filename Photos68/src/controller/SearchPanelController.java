package controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import model.Photo;
import model.Album;
import model.User;
import utils.DataManager;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SearchPanelController {

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

    @FXML
    private TextField tagTypeField;

    @FXML
    private TextField tagValueField;

    @FXML
private TextField secondTagTypeField;

@FXML
private TextField secondTagValueField;

@FXML
private ComboBox<String> searchLogicComboBox;


    @FXML
    private ListView<Photo> searchResultsListView;

    private List<Photo> allPhotos = new ArrayList<>();
    private AlbumsListController albumsListController;

    @FXML
    private void initialize() {
        try {
            for (User user : DataManager.loadUsers()) {
                for (Album album : user.getAlbums()) {
                    allPhotos.addAll(album.getPhotos());
                }
            }
            // Remove duplicate photos based on File object
            allPhotos = allPhotos.stream().distinct().collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
        }
        searchLogicComboBox.setItems(FXCollections.observableArrayList("AND", "OR"));
        searchLogicComboBox.getSelectionModel().selectFirst(); // Default to AND
    }
    @FXML
    private void handleSearchAction() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String tagType1 = tagTypeField.getText().trim();
        String tagValue1 = tagValueField.getText().trim();
        String tagType2 = secondTagTypeField.getText().trim();
        String tagValue2 = secondTagValueField.getText().trim();
    
        Set<File> encounteredFiles = new HashSet<>();
        String logic = searchLogicComboBox.getValue();

        List<Photo> filteredPhotos = allPhotos.stream()
            .filter(photo -> matchesCriteria(photo, startDate, endDate, tagType1, tagValue1, tagType2, tagValue2, logic))
            .filter(photo -> encounteredFiles.add(photo.getFile())) // Filter out duplicates
            .collect(Collectors.toList());
    
        searchResultsListView.getItems().setAll(filteredPhotos);
    }
    

    private boolean matchesCriteria(Photo photo, LocalDate startDate, LocalDate endDate, String tagType1, String tagValue1, String tagType2, String tagValue2, String logic) {
        boolean dateInRange = isDateInRange(photo.getDateTaken(), startDate, endDate);
        boolean tagMatches1 = tagMatches(photo, tagType1, tagValue1);
        boolean tagMatches2 = tagMatches(photo, tagType2, tagValue2);
    
        if ("AND".equals(logic)) {
            return dateInRange && (tagType1.isEmpty() || tagMatches1) && (tagType2.isEmpty() || tagMatches2);
        } else { // OR logic
            return dateInRange && ((tagType1.isEmpty() || tagMatches1) || (tagType2.isEmpty() || tagMatches2));
        }
    }
    private boolean tagMatches(Photo photo, String tagType, String tagValue) {
        if (!tagType.isEmpty() && !tagValue.isEmpty()) {
            String actualTagValue = photo.getTags().get(tagType);
            return tagValue.equals(actualTagValue);
        }
        return false;
    }
    

    private boolean isDateInRange(LocalDateTime photoDate, LocalDate startDate, LocalDate endDate) {
        LocalDate photoDateLocal = photoDate.toLocalDate();
        boolean isAfterStart = (startDate == null || photoDateLocal.isEqual(startDate) || photoDateLocal.isAfter(startDate));
        boolean isBeforeEnd = (endDate == null || photoDateLocal.isEqual(endDate) || photoDateLocal.isBefore(endDate));
        return isAfterStart && isBeforeEnd;
    }

    @FXML
    private void handleCreateAlbumAction() {
        List<Photo> selectedPhotos = new ArrayList<>(searchResultsListView.getItems());
        if (selectedPhotos.isEmpty()) {
            showAlert("No Photos Selected", "Please select photos to create an album.");
            return;
        }

        promptForAlbumName(selectedPhotos);
    }

    private void promptForAlbumName(List<Photo> photos) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Create New Album");
        dialog.setHeaderText("Enter Album Name");
        dialog.setContentText("Name:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (name.trim().isEmpty()) {
                showAlert("Error", "Album name cannot be blank.");
            } else if (isDuplicateAlbumName(name)) {
                showAlert("Error", "An album with this name already exists.");
            } else {
                createAndSaveAlbum(name, photos);
            }
        });
    }

    private void createAndSaveAlbum(String name, List<Photo> photos) {
        Album newAlbum = new Album(name);
        newAlbum.getPhotos().addAll(photos);
        User.currentUser.getAlbums().add(newAlbum);
        saveUserData();
        if (albumsListController != null) {
            albumsListController.refreshAlbumList();
        }
    }

    private void saveUserData() {
        try {
            List<User> users = DataManager.loadUsers();
            users.removeIf(user -> user.getUsername().equals(User.currentUser.getUsername()));
            users.add(User.currentUser);
            DataManager.saveUsers(users);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Failed to save data.");
        }
    }

    public void setAlbumsListController(AlbumsListController controller) {
        this.albumsListController = controller;
    }

    private boolean isDuplicateAlbumName(String name) {
        return User.currentUser.getAlbums().stream()
            .anyMatch(album -> album.getName().equalsIgnoreCase(name.trim()));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
