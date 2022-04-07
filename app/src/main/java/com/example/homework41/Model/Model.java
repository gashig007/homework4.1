package com.example.homework41.Model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
@Entity
public class Model implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private long create;
    private String title;


    public String getTitle() {
        return title;
    }

    public Model(long create, String title) {
        this.create = create;
        this.title = title;
    }

    public Model() {
    }

    public long getCreate() {
        return create;
    }

    public void setCreate(long create) {
        this.create = create;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
