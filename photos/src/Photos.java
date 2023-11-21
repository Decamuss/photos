import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import utils.DataManager;

import java.io.IOException;
import java.util.List;

public class Photos extends Application {

    private static List<User> users;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load users

        users = DataManager.loadUsers();
        

        Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        primaryStage.setTitle("Photo Application");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {

        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void setUsers(List<User> users) {
        Photos.users = users;
    }
}