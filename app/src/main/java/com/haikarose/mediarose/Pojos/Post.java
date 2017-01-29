package com.haikarose.mediarose.Pojos;

import android.os.Bundle;

/**
 * Created by root on 1/28/17.
 */

public class Post {

    public static final String DATE="DATE";
    public static final String MESSAGE="MESSAGE";
    public static final String RESOURCE="RESOURCE";



    private String date;

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    private String mesage;

    public String getResource() {
        return resource;
    }



    public void setResource(String resource) {
        this.resource = resource;
    }

    private String resource;

    public static Bundle postToBundle(Post post){

        Bundle bundle=new Bundle();
        bundle.putString(Post.DATE,post.getDate());
        bundle.putString(Post.MESSAGE,post.getMesage());
        bundle.putString(Post.RESOURCE,post.getResource());

        return bundle;
    }

    public static Post postFromBundle(Bundle bundle){


        Post post=new Post();
        post.setMesage(bundle.getString(Post.MESSAGE));
        post.setDate(bundle.getString(Post.DATE));
        post.setResource(bundle.getString(Post.RESOURCE));
        return post;

    }

}
