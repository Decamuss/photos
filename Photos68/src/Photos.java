import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;
import utils.DataManager;

import java.io.IOException;
import java.util.List;

/**
 * The main application class for the Photo Application.
 * Extends the JavaFX Application class to create a JavaFX application.
 */
public class Photos extends Application {

    private static List<User> users;

    /**
     * The entry point of the application.
     *  main passes into this method to start the app visualization
     * @param args The command line arguments passed to the application.
     */
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

    /**
     * Starts the JavaFX application.
     *
     * @param args is the primaryStage, The primary stage for this application, onto which the application scene can be set.
     * @throws IOException If an error occurs during the loading of the FXML file.
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Gets the list of users currently loaded in the application.
     *
     * @return The list of users.
     */
    public static List<User> getUsers() {
        return users;
    }

    /**
     * Sets the list of users for the application.
     *
     * @param users The list of users to be set.
     */
    public static void setUsers(List<User> users) {
        Photos.users = users;
    }
}
