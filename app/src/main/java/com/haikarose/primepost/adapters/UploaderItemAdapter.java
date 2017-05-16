package com.haikarose.primepost.adapters;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.haikarose.primepost.Pojos.Uploader;
import com.haikarose.primepost.R;
import com.haikarose.primepost.activities.PostsScrollingActivity;
import com.haikarose.primepost.tools.DateHelper;
import com.haikarose.primepost.tools.StringUpperHelper;

import java.util.List;


/**
 * Created by root on 1/28/17.
 */

public class UploaderItemAdapter extends RecyclerView.Adapter<UploaderItemAdapter.ItemHolder> {

    private List<Object> items;
    private Context firstContext;

    public UploaderItemAdapter(Context context, List<Object> items){
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

        Uploader uploader=(Uploader) items.get(position);
        holder.setData(uploader);

    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private Uploader uploaderObject;
        private TextView date;
        private TextView more_info;
        private TextView uploader;
        private TextView message;
        private Context context;

        public ItemHolder(View view){
            super(view);
            context=view.getContext();
            view.setOnClickListener(this);
            this.date=(TextView)view.findViewById(R.id.time);
            this.more_info=(TextView)view.findViewById(R.id.more_info);
            this.message=(TextView)view.findViewById(R.id.description);
            this.uploader=(TextView)view.findViewById(R.id.uploader);
        }

        public void setData(Uploader uploaderO){
            this.uploaderObject=uploaderO;
            date.setText(DateHelper.getPresentableDate(""));
            message.setText(uploaderObject.getDetails());
            uploader.setText(StringUpperHelper.doUpperlization(uploaderObject.getName().toUpperCase()));
            if(uploaderObject.getPostCount()>1){
                more_info.setText(Integer.toString(uploaderObject.getPostCount()));
            }else{
                more_info.setText("0");
            }

        }

        @Override
        public void onClick(View v) {
                Intent intent;

                if(v.getId()==R.id.image){
                    intent=new Intent(firstContext,PostsScrollingActivity.class);
                    intent.putExtra(Uploader.ID,uploaderObject.getId());
                    ItemHolder.this.context.startActivity(intent);
                }else{
                    //do something when the button is clicked.
                    intent=new Intent(firstContext,PostsScrollingActivity.class);
                    intent.putExtra(Uploader.ID,uploaderObject.getId());
                    ItemHolder.this.context.startActivity(intent);
                }


        }
    }
}
