package utils;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.User;

/**
 * Utility class for managing user data, providing methods to save, load, add, and delete user information.
 */
public class DataManager {

    private static final String FILE_PATH = "data/users.ser"; // Updated file path
    /**
     * Saves a list of users to the specified file path.
     *
     * @param users The list of users to be saved.
     * @throws IOException If an I/O error occurs during the serialization process.
     */
    public static void saveUsers(List<User> users) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(users);
        }
    }

    /**
     * Loads a list of users from the specified file path.
     * If the file does not exist, creates a new file and returns an empty list.
     *
     * @return The list of users loaded from the file or an empty list if the file is empty or not found.
     * @throws IOException            If an I/O error occurs during the deserialization process.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public static List<User> loadUsers() throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs(); // Create parent directories if they don't exist
            file.createNewFile(); // Create a new empty file 'users.ser'
            return new ArrayList<>(); // Return an empty list of type User
        }
    
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            Object readObject = in.readObject();
            if (readObject instanceof List) {
                return (List<User>) readObject;
            } else {
                return new ArrayList<>(); // Return an empty list if the file doesn't contain a List object
            }
        } catch (EOFException | ClassNotFoundException e) {
            return new ArrayList<>(); // Return an empty list if there is an EOFException or ClassNotFoundException
        }
    }
    
    /**
     * Adds a user to the list of users and saves the updated list.
     *
     * @param user The user to be added.
     */
    public static void addUser(User user) {
        try {
            List<User> users = loadUsers();
            users.add(user);
            saveUsers(users);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle IOException (e.g., log the error, notify the user)
        }
    }
    
    /**
     * Deletes a user with the specified username from the list of users and saves the updated list.
     *
     * @param username The username of the user to be deleted.
     * @throws IOException            If an I/O error occurs during the serialization process.
     * @throws ClassNotFoundException If the class of a serialized object cannot be found.
     */
    public static void deleteUser(String username) throws IOException, ClassNotFoundException {
        List<User> users = loadUsers();
        users = users.stream().filter(u -> !u.getUsername().equals(username)).collect(Collectors.toList());
        saveUsers(users);
    }
}
