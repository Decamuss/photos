package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an album in the Photo Application.
 * Each album has a name and a list of associated photos.
 */
public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private List<Photo> photos; // Assuming you have a Photo class
    public static Album currentAlbum;

    /**
     * Constructs a new album with the specified name.
     *
     * @param name The name of the album.
     */
    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<Photo>();
    }

    /**
     * Gets the name of the album.
     *
     * @return The name of the album.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the album.
     *
     * @param name The new name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the list of photos associated with the album.
     *
     * @return The list of photos.
     */
    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     * Sets the list of photos for the album.
     *
     * @param photos The new list of photos to set.
     */
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    /**
     * Returns a string representation of the album.
     *
     * @return The name of the album.
     */
    @Override
    public String toString() {
       
        return name;
    }

    /**
     * Removes a photo from the list of photos associated with the album.
     *
     * @param photo The photo to remove.
     */
    public void removePhoto(Photo photo)
    {
        this.photos.remove(photo);
    }

    /**
     * Adds a photo to the list of photos associated with the album.
     *
     * @param photo The photo to add.
     */
     public void addPhoto(Photo photo)
    {
        this.photos.add(photo);
    }
    // Add more methods as needed
}
