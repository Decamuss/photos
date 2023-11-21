package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Album implements Serializable {
    private static final long serialVersionUID = 1L;
    private String name;
    private List<Photo> photos; // Assuming you have a Photo class
    public static Album currentAlbum;

    public Album(String name) {
        this.name = name;
        this.photos = new ArrayList<Photo>();
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public String toString() {
       
        return name;
    }

    public void removePhoto(Photo photo)
    {
        this.photos.remove(photo);
    }

     public void addPhoto(Photo photo)
    {
        this.photos.add(photo);
    }
    // Add more methods as needed
}