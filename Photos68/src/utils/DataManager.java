package utils;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import model.User;

public class DataManager {

    private static final String FILE_PATH = "photos/data/users.ser"; // Updated file path

    public static void saveUsers(List<User> users) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(users);
        }
    }

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
    

    public static void deleteUser(String username) throws IOException, ClassNotFoundException {
        List<User> users = loadUsers();
        users = users.stream().filter(u -> !u.getUsername().equals(username)).collect(Collectors.toList());
        saveUsers(users);
    }
}
