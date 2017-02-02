package com.haikarose.mediarose.Pojos;

/**
 * Created by root on 1/31/17.
 */

public class PostImageItem {

    private String url;
    private String type;
    public static final String EXCHANGE_RES_ID="EXCHANGE_RES_ID";

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setType(String type){
        this.type=type;
    }

    public String getType(){
        return this.type;
    }

    public Post toPost(PostImageItem item){
        Post post=new Post();
        post.setResource(item.getUrl());
        return post;
    }
}
