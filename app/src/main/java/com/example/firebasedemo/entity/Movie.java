package com.example.firebasedemo.entity;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;

public class Movie implements Serializable {
    @DocumentId
    private String id;
    private String title;
    private String description;
    private String genre;
    private String posterUrl;
    private int duration;

    public Movie() {}

    public Movie(String title, String description, String genre, String posterUrl, int duration) {
        this.title = title;
        this.description = description;
        this.genre = genre;
        this.posterUrl = posterUrl;
        this.duration = duration;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
    public String getPosterUrl() { return posterUrl; }
    public void setPosterUrl(String posterUrl) { this.posterUrl = posterUrl; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }
}
