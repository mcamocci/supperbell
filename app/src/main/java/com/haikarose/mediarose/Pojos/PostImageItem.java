package com.haikarose.mediarose.Pojos;

/**
 * Created by root on 1/31/17.
 */

public class PostImageItem {

    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    public Post toPost(PostImageItem item){
        Post post=new Post();
        post.setResource(item.getUrl());
        return post;
    }
}
