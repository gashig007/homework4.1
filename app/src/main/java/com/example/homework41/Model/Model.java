package com.example.homework41.Model;

import java.io.Serializable;

public class Model implements Serializable {
    private long create;
    private String title;


    public String getTitle() {
        return title;
    }

    public Model(long create, String title) {
        this.create = create;
        this.title = title;
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
