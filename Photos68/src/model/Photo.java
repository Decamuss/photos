package model;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Represents a photograph in the Photo Application.
 * Each photo has a file, date taken, caption, and tags associated with it.
 */
public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private File file;
    private LocalDateTime dateTaken;
    private String caption;
    private Map<String, String> tags; // Map to store tags with their values

    public static Photo tempMove;
    public static Photo tempCopy;
    public static Photo currentPhoto;

    /**
     * Constructs a new photo with the specified file.
     *
     * @param file The file associated with the photo.
     */
    public Photo(File file) {
        this.file = file;
        this.dateTaken = getLastModifiedDate();
        this.caption = ""; // Initialize with empty caption
        this.tags = new HashMap<>(); // Initialize the tags map
    }

    /**
     * Gets the last modified date of the photo file.
     *
     * @return The last modified date as a LocalDateTime object.
     */
    private LocalDateTime getLastModifiedDate() {
        long lastModifiedTime = file.lastModified();
        return LocalDateTime.ofInstant(new Date(lastModifiedTime).toInstant(), TimeZone.getDefault().toZoneId());
    }

    /**
     * Sets the caption for the photo.
     *
     * @param caption The new caption to set.
     */
    public void setCaption(String caption) {
        this.caption = caption;
    }

    /**
     * Gets the caption of the photo.
     *
     * @return The caption of the photo.
     */
    public String getCaption() {
        return this.caption;
    }

    /**
     * Gets the file associated with the photo.
     *
     * @return The file associated with the photo.
     */
    public File getFile() {
        return this.file;
    }

    /**
     * Gets the date and time the photo was taken.
     *
     * @return The date and time the photo was taken as a LocalDateTime object.
     */
    public LocalDateTime getDateTaken() {
        return dateTaken;
    }

    /**
     * Adds a tag to the photo with the specified type and value.
     *
     * @param type  The type of the tag.
     * @param value The value of the tag.
     */
    public void addTag(String type, String value) {
        tags.put(type, value);
    }

    /**
     * Removes a tag from the photo with the specified type and value.
     *
     * @param type  The type of the tag to remove.
     * @param value The value of the tag to remove.
     */
    public void removeTag(String type, String value) {
        tags.entrySet().removeIf(entry -> entry.getKey().equals(type) && entry.getValue().equals(value));
    }
    
    
    /**
     * Gets the keys of all tags associated with the photo.
     *
     * @return A set of tag keys.
     */
    public Set<String> getTagKeys() {
        return tags.keySet();
    }

    /**
     * Gets the values of all tags associated with the photo.
     *
     * @return A collection of tag values.
     */
    public Collection<String> getTagValues() {
        return tags.values();
    }

    /**
     * Gets a map of all tags associated with the photo.
     *
     * @return A map of tags with their values.
     */
    public Map<String, String> getTags() {
        return tags;
    }

        /**
     * Returns a string representation of the photo.
     *
     * @return The string representation of the photo.
     */
    @Override
    public String toString() {
        return this.caption + " [" + dateTaken + "]";
    }

    
}
