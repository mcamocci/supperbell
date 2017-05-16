package com.haikarose.primepost.adapters;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.haikarose.primepost.Pojos.Post;
import com.haikarose.primepost.R;
import com.haikarose.primepost.activities.PostDetailActivity;
import com.haikarose.primepost.tools.DateHelper;
import com.haikarose.primepost.tools.StringUpperHelper;
import com.haikarose.primepost.tools.TransferrableContent;

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
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.poster_post,parent,false);
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
        private TextView uploader;
        private TextView message;
        private ImageView more_menu;
        private Context context;

        public ItemHolder(View view){
            super(view);
            context=view.getContext();
            view.setOnClickListener(this);
            this.date=(TextView)view.findViewById(R.id.time);
            this.message=(TextView)view.findViewById(R.id.description);
            this.uploader=(TextView)view.findViewById(R.id.uploader);
            this.more_menu=(ImageView)view.findViewById(R.id.more_menu);
            more_menu.setOnClickListener(this);
        }

        public void setData(Post post){
            this.post=post;
            date.setText(DateHelper.getPresentableDate(post.getDate().toString()));
            message.setText(StringUpperHelper.doUpperlization(post.getContent()));
            uploader.setText(StringUpperHelper.doUpperlization(post.getName()));
        }

        @Override
        public void onClick(View v) {
                Intent intent;

                if(v.getId()==R.id.image){
                    intent=new Intent(firstContext, PostDetailActivity.class);
                    intent.putExtra(Post.EXCHANGE_ID, TransferrableContent.toJsonObject(post));
                    ItemHolder.this.context.startActivity(intent);
                }else if(v.getId()==R.id.more_menu){

                    PopupMenu popupMenu=new PopupMenu(context,v);
                    popupMenu.getMenuInflater().inflate(R.menu.popup_menu,popupMenu.getMenu());
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if(item.getTitle().equals("Open")){
                                Intent intent=new Intent(firstContext, PostDetailActivity.class);
                                intent.putExtra(Post.EXCHANGE_ID, TransferrableContent.toJsonObject(post));
                                ItemHolder.this.context.startActivity(intent);
                            }else if(item.getTitle().equals("Share")){
                                Toast.makeText(context,"share",Toast.LENGTH_LONG).show();
                            }
                            return false;
                        }
                    });

                    popupMenu.show();

                }else{
                    intent=new Intent(firstContext, PostDetailActivity.class);
                    intent.putExtra(Post.EXCHANGE_ID, TransferrableContent.toJsonObject(post));
                    ItemHolder.this.context.startActivity(intent);
                }


        }
    }
}
