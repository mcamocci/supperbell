package com.haikarose.primepost.adapters;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.haikarose.primepost.Pojos.PostImageItem;
import com.haikarose.primepost.R;
import com.haikarose.primepost.activities.ImageViewerActivity;
import com.haikarose.primepost.tools.FileDownloadOperation;
import com.haikarose.primepost.tools.FileTypeHelper;
import com.haikarose.primepost.tools.StringUpperHelper;
import com.haikarose.primepost.tools.TransferrableContent;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.DOWNLOAD_SERVICE;
/**
 * Created by root on 3/13/17.
 */

public class DownloadItemAdapter extends RecyclerView.Adapter<DownloadItemAdapter.NoteViewHolder> {

    private List<Object> content;
    private Context context;

    public DownloadItemAdapter(Context context, List<Object> list){
        this.context=context;
        this.content=list;
    }

    @Override
    public NoteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.download_view,parent,false);
        NoteViewHolder holder=new NoteViewHolder(view,context);
        return holder;
    }

    @Override
    public void onBindViewHolder(NoteViewHolder holder, int position) {
        PostImageItem item=(PostImageItem)content.get(position);
        holder.setData(item);
    }

    @Override
    public int getItemCount() {
        return content.size();
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView type_image_holder;
        private TextView title;
        private TextView preview;
        private TextView share;
        private TextView download;
        private Context context;
        private long enqueue;
        private DownloadManager dm;
        private PostImageItem downloadableItem;

        private String type;

        public NoteViewHolder(View view,Context context){
            super(view);
            title=(TextView)view.findViewById(R.id.title);
            preview=(TextView)view.findViewById(R.id.preview);
            share=(TextView)view.findViewById(R.id.share);
            download=(TextView)view.findViewById(R.id.download);
            type_image_holder=(ImageView) view.findViewById(R.id.type_image_holder);
            preview.setOnClickListener(this);
            share.setOnClickListener(this);
            download.setOnClickListener(this);
            this.context=context;

        }

        public void setData(PostImageItem item){
            this.downloadableItem=item;

            if(Arrays.asList(FileTypeHelper.imagesList).contains(item.getType().toUpperCase())){
                type_image_holder.setImageResource(R.drawable.ic_image);
                preview.setVisibility(View.VISIBLE);
            }else{
                type_image_holder.setImageResource(R.drawable.ic_attach_file);
                preview.setVisibility(View.GONE);
            }

            File file=new File(downloadableItem.getUrl());
            title.setText(StringUpperHelper.doUpperlization(FilenameUtils.getBaseName(downloadableItem.getUrl())));

            if(FileDownloadOperation.isFileAvaillable(context,file)&&
                    !Arrays.asList(FileTypeHelper.imagesList).contains(item.getType().toUpperCase())){
                download.setText("Open");
            }else if(FileDownloadOperation.isFileAvaillable(context,file)&&
                    Arrays.asList(FileTypeHelper.imagesList).contains(item.getType().toUpperCase())){
                preview.setVisibility(View.INVISIBLE);
                download.setText("Open");
            }else{
                download.setText("Download");
                preview.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent=null;
            if(v.getId()==R.id.download){

                File file=new File(downloadableItem.getUrl());

                if(FileDownloadOperation.isFileAvaillable(context,file)){
                    MimeTypeMap myMime = MimeTypeMap.getSingleton();
                    Intent newIntent = new Intent(Intent.ACTION_VIEW);
                    String mimeType = myMime.getMimeTypeFromExtension(fileExt(file.getAbsolutePath()).substring(1));
                    newIntent.setDataAndType(Uri.fromFile(
                            new File(FileDownloadOperation.downloadToFolder(context)
                    +File.separator+file.getName())),mimeType);
                    newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        context.startActivity(newIntent);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(context, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                    }
                }else{

                    Toast.makeText(context,"Download started",Toast.LENGTH_LONG).show();
                    dm = (DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);

                    DownloadManager.Request request = new DownloadManager.Request(
                            Uri.parse(downloadableItem.getUrl()));

                    String folder=null;

                    if((folder= FileDownloadOperation.downloadToFolder(context))!=null){

                        Uri downloadLocation=Uri.fromFile(new File(folder, file.getName()));
                        request.setDestinationUri(downloadLocation);
                        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        enqueue = dm.enqueue(request);

                    }else{
                        Toast.makeText(context,"Something isn't wright",Toast.LENGTH_SHORT).show();
                    }
                }

            }else if(v.getId()==R.id.share){
             /*   String shared_content=downloadableItem.getName()+". "+downloadableItem.getDescription()+" documents are now availlable on desapoint" +
                        " Get it on google play today." +
                        " https://play.google.com/store/apps/details?id=com.haikarose.primepost to access this file";
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,shared_content);
                Intent cooler_one=intent.createChooser(intent,"Share file using:-");
                cooler_one.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(cooler_one);*/
            }else if(v.getId()==R.id.preview){
                intent = new Intent(context, ImageViewerActivity.class);
                PostImageItem imageItem=new PostImageItem();
                imageItem.setUrl(downloadableItem.getUrl());
                imageItem.setType(downloadableItem.getType());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra(PostImageItem.EXCHANGE_RES_ID, TransferrableContent.toJsonObject(imageItem));
                context.startActivity(intent);
            }
        }
    }

    private String fileExt(String url) {
        if (url.indexOf("?") > -1) {
            url = url.substring(0, url.indexOf("?"));
        }
        if (url.lastIndexOf(".") == -1) {
            return null;
        } else {
            String ext = url.substring(url.lastIndexOf(".") + 1);
            if (ext.indexOf("%") > -1) {
                ext = ext.substring(0, ext.indexOf("%"));
            }
            if (ext.indexOf("/") > -1) {
                ext = ext.substring(0, ext.indexOf("/"));
            }
            return ext.toLowerCase();

        }
    }
}
