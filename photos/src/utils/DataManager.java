package utils;

import model.User;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public static void saveUsers(List<User> users, String filePath) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
            out.writeObject(users);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<User> loadUsers(String filePath) throws IOException, ClassNotFoundException {
        File file = new File(filePath);
        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filePath))) {
            return (List<User>) in.readObject();
        }
    }
}
