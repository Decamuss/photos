package model;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private File file;
    private LocalDateTime dateTaken;
    private String caption;
    private Map<String, String> tags; // Map to store tags with their values

    public static Photo tempMove;
    public static Photo tempCopy;
    public static Photo currentPhoto;

    public Photo(File file) {
        this.file = file;
        this.dateTaken = getLastModifiedDate();
        this.caption = ""; // Initialize with empty caption
        this.tags = new HashMap<>(); // Initialize the tags map
    }

    private LocalDateTime getLastModifiedDate() {
        long lastModifiedTime = file.lastModified();
        return LocalDateTime.ofInstant(new Date(lastModifiedTime).toInstant(), TimeZone.getDefault().toZoneId());
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return this.caption;
    }

    public File getFile() {
        return this.file;
    }

    public LocalDateTime getDateTaken() {
        return dateTaken;
    }

    public void addTag(String type, String value) {
        tags.put(type, value);
    }

    public void removeTag(String type, String value) {
        tags.remove(type);
    }

    public Set<String> getTagKeys() {
        return tags.keySet();
    }

    public Collection<String> getTagValues() {
        return tags.values();
    }

    public Map<String, String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return this.caption + " [" + dateTaken + "]";
    }

    // Additional methods as needed
}
