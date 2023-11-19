import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import utils.DataManager;
import model.User;

import java.io.IOException;
import java.util.List;

public class App extends Application {

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
            // Save users when closing application
            try {
                DataManager.saveUsers(users);
            } catch (IOException e) {
                e.printStackTrace();
                // Handle the exception, maybe show an alert to the user
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static List<User> getUsers() {
        return users;
    }

    public static void setUsers(List<User> users) {
        App.users = users;
    }
}
