package com.example.firebasedemo.entity;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;

public class Showtime implements Serializable {
    @DocumentId
    private String id;
    private String movieId;
    private String theaterId;
    private String theaterName;
    private String time;
    private String date;
    private double price;

    public Showtime() {}

    public Showtime(String movieId, String theaterId, String theaterName, String time, String date, double price) {
        this.movieId = movieId;
        this.theaterId = theaterId;
        this.theaterName = theaterName;
        this.time = time;
        this.date = date;
        this.price = price;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getMovieId() { return movieId; }
    public void setMovieId(String movieId) { this.movieId = movieId; }
    public String getTheaterId() { return theaterId; }
    public void setTheaterId(String theaterId) { this.theaterId = theaterId; }
    public String getTheaterName() { return theaterName; }
    public void setTheaterName(String theaterName) { this.theaterName = theaterName; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
