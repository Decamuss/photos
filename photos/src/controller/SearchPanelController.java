package controller;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert;
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
    }

 @FXML
    private void handleSearchAction() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        String tagType = tagTypeField.getText().trim();
        String tagValue = tagValueField.getText().trim();

        Set<File> encounteredFiles = new HashSet<>();

        List<Photo> filteredPhotos = allPhotos.stream()
            .filter(photo -> matchesCriteria(photo, startDate, endDate, tagType, tagValue))
            .filter(photo -> encounteredFiles.add(photo.getFile())) // Filter out duplicates
            .collect(Collectors.toList());

        searchResultsListView.getItems().setAll(filteredPhotos);
    }


    private boolean matchesCriteria(Photo photo, LocalDate startDate, LocalDate endDate, String tagType, String tagValue) {
        boolean dateInRange = isDateInRange(photo.getDateTaken(), startDate, endDate);
        boolean tagMatches = false;
        if (!tagType.isEmpty() && !tagValue.isEmpty()) {
            // Check if the tag type exists and its value matches the specified tag value
            String actualTagValue = photo.getTags().get(tagType);
            tagMatches = tagValue.equals(actualTagValue);
        }
        return dateInRange && (tagType.isEmpty() || tagMatches);
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
