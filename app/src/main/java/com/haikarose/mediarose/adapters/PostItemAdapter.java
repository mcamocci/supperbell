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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.cat_item,parent,false);
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
        private TextView message;
        private LinearLayout button;
        private ImageView promoImage;
        private Context context;

        public ItemHolder(View view){
            super(view);
            context=view.getContext();
            view.setOnClickListener(this);
            this.date=(TextView)view.findViewById(R.id.time);
            this.message=(TextView)view.findViewById(R.id.description);
            this.promoImage=(ImageView)view.findViewById(R.id.image);
            promoImage.setOnClickListener(this);
           // this.button=(LinearLayout)view.findViewById(R.id.button);
        }

        public void setData(Post post){
            this.post=post;
            date.setText(post.getDate().toString());
            message.setText(post.getMesage());
            if(post.getResource()!=null){
                promoImage.setVisibility(View.VISIBLE);
                try{
                    URL url=new URL("https://scontent-bru2-1.xx.fbcdn.net/v/t1.0-9/16195034_760896287395877_1216980308058827388_n.jpg?_nc_eui2=v1%3AAeEeVhV4-89wpti2Qi5yaJsvu2XzIT6shc3RTzXkXXDy_Thk8pmzWvBf4xEnwsBHLmoiptK1Wycnx9HBViapuDq2O3Y31Ch22yDffkseEfRTKw&oh=d835e32fb126bb13229fd01de2d63bba&oe=58FEF358");
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
                    Bundle post_bundle=Post.postToBundle(post);
                    intent.putExtras(post_bundle);
                    ItemHolder.this.context.startActivity(intent);
                }else{
                    //do something when the button is clicked.
                    intent=new Intent(firstContext, PostDetailActivity.class);
                    Bundle post_bundle=Post.postToBundle(post);
                    intent.putExtras(post_bundle);
                    ItemHolder.this.context.startActivity(intent);
                }


        }
    }
}
