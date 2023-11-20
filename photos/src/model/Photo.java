package model;

import java.io.File;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private File file;
    private Date dateTaken;
    private String caption;
    public static Photo tempMove;
    public static Photo tempCopy;
    public static Photo currentPhoto;
    // Add more fields as needed, like tags

    public Photo(File file) {
        this.file = file;
        this.dateTaken = getLastModifiedDate();
        this.caption = "photo1";
        // Initialize other fields
    }

   private Date getLastModifiedDate() {
        Calendar cal = Calendar.getInstance();
        long lastModifiedTime = file.lastModified();
        cal.setTimeInMillis(lastModifiedTime);
        return cal.getTime();
    }

    @Override
    public String toString() {
        return this.caption;
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


    // Getters and setters
    // Add methods for handling tags
}
