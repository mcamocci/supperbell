package com.haikarose.primepost.Pojos;

/**
 * Created by root on 5/7/17.
 */

public class Uploader {

    public static final String ID="UPLOADER_ID";
    private int id;
    private String poster_name;
    private String description;
    private int posts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return poster_name;
    }

    public void setName(String name) {
        this.poster_name = name;
    }

    public String getDetails() {
        return description;
    }

    public void setDetails(String details) {
        this.description = details;
    }

    public int getPostCount() {
        return posts;
    }

    public void setPostCount(int postCount) {
        this.posts = postCount;
    }
}
