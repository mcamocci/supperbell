package com.haikarose.mediarose.adapters;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.haikarose.mediarose.Pojos.Post;
import com.haikarose.mediarose.R;
import com.haikarose.mediarose.activities.ImageViewerActivity;
import com.haikarose.mediarose.activities.PostDetailActivity;
import com.haikarose.mediarose.tools.DateHelper;
import com.haikarose.mediarose.tools.TransferrableContent;

import java.io.PipedOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;


/**
 * Created by root on 1/28/17.
 */

public class PostItemAdapter extends RecyclerView.Adapter<PostItemAdapter.ItemHolder> {

    private List<Object> items;
    private Context firstContext;

    public PostItemAdapter(Context context,List<Object> items){
        this.items=items;
        this.firstContext=context;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item,parent,false);
        ItemHolder holder=new ItemHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {

        Post post=(Post)items.get(position);
        holder.setData(post);

    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Post post;
        private TextView date;
        private TextView more_info;
        private TextView uploader;
        private TextView message;
        private LinearLayout button;
        private ImageView promoImage;
        private Context context;

        public ItemHolder(View view){
            super(view);
            context=view.getContext();
            view.setOnClickListener(this);
            this.date=(TextView)view.findViewById(R.id.time);
            this.more_info=(TextView)view.findViewById(R.id.more_info);
            this.message=(TextView)view.findViewById(R.id.description);
            this.uploader=(TextView)view.findViewById(R.id.uploader);
            this.promoImage=(ImageView)view.findViewById(R.id.image);
            promoImage.setOnClickListener(this);
           // this.button=(LinearLayout)view.findViewById(R.id.button);
        }

        public void setData(Post post){
            this.post=post;
            date.setText(DateHelper.getPresentableDate(post.getDate().toString()));
            message.setText(post.getContent());
            uploader.setText(post.getName());
            if(post.getResources().size()>1){
                more_info.setText(Integer.toString(post.getResources().size()-1)+" "+context.getResources().getString(R.string.eye_label));
            }else{
                more_info.setText(context.getResources().getString(R.string.eye_label_empty));
            }
            if(post.getResources().size()>0 && post.getResources().get(0).getType().equalsIgnoreCase(Post.TYPE_IMAGE)){
                promoImage.setVisibility(View.VISIBLE);
                try{
                    URL url=new URL(post.getResources().get(0).getUrl());
                    Glide.with(firstContext).load(url.toString()).centerCrop().placeholder(android.R.drawable.editbox_dropdown_light_frame).into(promoImage);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }else{
                promoImage.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
                Intent intent;

                if(v.getId()==R.id.image){
                    intent=new Intent(firstContext, ImageViewerActivity.class);
                    intent.putExtra(Post.EXCHANGE_ID, TransferrableContent.toJsonObject(post));
                    ItemHolder.this.context.startActivity(intent);
                }else{
                    //do something when the button is clicked.
                    intent=new Intent(firstContext, PostDetailActivity.class);
                    intent.putExtra(Post.EXCHANGE_ID, TransferrableContent.toJsonObject(post));
                    ItemHolder.this.context.startActivity(intent);
                }


        }
    }
}