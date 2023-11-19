package utils;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DataManager {

    private static final String FILE_PATH = "path_to_your_data_file"; // Update this with your actual file path

    public static void saveUsers(List<User> users) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            out.writeObject(users);
        }
    }

    public static List<User> loadUsers() throws IOException, ClassNotFoundException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            return (List<User>) in.readObject();
        }
    }

    public static void addUser(User user) throws IOException, ClassNotFoundException {
        List<User> users = loadUsers();
        users.add(user);
        saveUsers(users);
    }

    public static void deleteUser(String username) throws IOException, ClassNotFoundException {
        List<User> users = loadUsers();
        users = users.stream().filter(u -> !u.getUsername().equals(username)).collect(Collectors.toList());
        saveUsers(users);
    }
}
