package com.haikarose.mediarose.Pojos;

import android.os.Bundle;

import java.util.List;

/**
 * Created by root on 1/28/17.
 */

public class Post {

    public static final String DATE="DATE";
    public static final String MESSAGE="MESSAGE";
    public static final String RESOURCE="RESOURCE";
    public static final String PAGE="page";
    public static final String COUNT="count";
    public static final String TYPE_IMAGE="IMAGE";
    public static final String EXCHANGE_ID="EXCHANGE_ID";



    private String date;
    private String resource;
    private String content;
    private String uploader;
    private int id;
    private List<PostImageItem> resources;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }



    public String getResource() {
        return resource;
    }



    public void setResource(String resource) {
        this.resource = resource;
    }



    public static Bundle postToBundle(Post post){

        Bundle bundle=new Bundle();
        bundle.putString(Post.DATE,post.getDate());
        bundle.putString(Post.MESSAGE,post.getContent());
        bundle.putString(Post.RESOURCE,post.getResource());

        return bundle;
    }

    public static Post postFromBundle(Bundle bundle){

        Post post=new Post();
        post.setContent(bundle.getString(Post.MESSAGE));
        post.setDate(bundle.getString(Post.DATE));
        post.setResource(bundle.getString(Post.RESOURCE));
        return post;

    }

    public void addResourceList(PostImageItem resource){
        this.resources.add(resource);
    }

    public void setResourcesList(List<PostImageItem> resources){
        this.resources=resources;
    }

    public List<PostImageItem> getResources(){
        return this.resources;
    }

    public void setId(int id){
        this.id=id;
    }

    public int getId(){
        return this.id;
    }

    public String getName(){
        return this.uploader;
    }
    public void setName(String name){
        this.uploader=name;
    }

}
