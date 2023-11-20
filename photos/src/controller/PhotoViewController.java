package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;

public class PhotoViewController {

    @FXML
    private Button AddTagButton;

    @FXML
    private Label Caption;

    @FXML
    private Label Date;

    @FXML
    private ImageView PhotoPreview;

    @FXML
    private Button ReturnButton;

    @FXML
    private TableColumn<?, ?> TagName;

    @FXML
    private TableView<?> TagTable;

    @FXML
    private TableColumn<?, ?> TagValue;

    @FXML
    void AddTagRequest(ActionEvent event) {

    }

    @FXML
    void PreviousScreenRequest(ActionEvent event) {

    }

}
