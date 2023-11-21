package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the Photo Application.
 * Each user has a username and a list of albums.
 */
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String username;
    private List<Album> albums;
    public static User currentUser;
     
    /**
     * Constructs a new user with the specified username.
     *
     * @param username The username of the user.
     */
    public User(String username) {
        this.username = username;
        this.albums = new ArrayList<Album>();
    }

    /**
     * Gets the username of the user.
     *
     * @return The username of the user.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the username of the user.
     *
     * @param username The new username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Gets the list of albums associated with the user.
     *
     * @return The list of albums.
     */
    public List<Album> getAlbums() {
        return albums;
    }

    /**
     * Sets the list of albums for the user.
     *
     * @param albums The new list of albums to set.
     */
    public void setAlbums(List<Album> albums) {
        this.albums = albums;
    }

    /**
     * Adds an album to the list of albums for the user.
     *
     * @param album The album to add.
     */
    public void addAlbums(Album album){
        this.albums.add(album);
    }

    /**
     * Removes an album from the list of albums for the user.
     *
     * @param album The album to remove.
     */
    public void removeAlbum(Album album)
    {
        this.albums.remove(album);
    }

}
