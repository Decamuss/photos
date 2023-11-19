package model;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Photo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String filePath;
    private LocalDateTime dateTaken;
    private String caption;
    // Add more fields as needed, like tags

    public Photo(String filePath, LocalDateTime dateTaken) {
        this.filePath = filePath;
        this.dateTaken = dateTaken;
        this.caption = "";
        // Initialize other fields
    }

    // Getters and setters
    // Add methods for handling tags
}
