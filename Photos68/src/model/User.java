package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private List<Album> albums;
    public static User currentUser;

    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<Album>();
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Album> getAlbums() {
        return albums;
    }

    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    public void addAlbums(Album album){
        this.albums.add(album);
    }

    public void removeAlbum(Album album)
    {
        this.albums.remove(album);
    }

    // Add more methods as needed
}
