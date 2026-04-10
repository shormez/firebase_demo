package com.example.firebasedemo.entity;

import com.google.firebase.firestore.DocumentId;
import java.io.Serializable;

public class Theater implements Serializable {
    @DocumentId
    private String id;
    private String name;
    private String location;

    public Theater() {}

    public Theater(String name, String location) {
        this.name = name;
        this.location = location;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
}
