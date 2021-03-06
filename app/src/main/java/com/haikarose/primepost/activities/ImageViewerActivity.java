package com.haikarose.primepost.activities;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.haikarose.primepost.Pojos.PostImageItem;
import com.haikarose.primepost.R;
import com.haikarose.primepost.tools.ConnectionChecker;
import com.haikarose.primepost.tools.TransferrableContent;
import com.vungle.publisher.VunglePub;

public class ImageViewerActivity extends AppCompatActivity {

    // get the VunglePub instance
    VunglePub vunglePub = VunglePub.getInstance();
    private PostImageItem postImageItem;
    DownloadManager downloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        if (!(ConnectionChecker.isInternetConnected(getBaseContext()))) {
            Intent intent=new Intent(getBaseContext(),NoConnectionActivity.class);
            startActivity(intent);
        }
       /* rewardedVideoAd= MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);*//*
        doVungleWay();
        loadAd();*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarTitle("Haikarosetz explore");
        getSupportActionBar().setIcon(R.drawable.ic_action_camera);


        ImageView promo_image = (ImageView) findViewById(R.id.imageView2);
        Context context = getBaseContext();
        postImageItem= TransferrableContent.fromJsonToPostImageItem(getIntent().getStringExtra(PostImageItem.EXCHANGE_RES_ID));
        Glide.with(context).load(postImageItem.getUrl()).placeholder(android.R.drawable.editbox_dropdown_light_frame).into(promo_image);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.app_gallery_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!(ConnectionChecker.isInternetConnected(getBaseContext()))) {
            Intent intent=new Intent(getBaseContext(),NoConnectionActivity.class);
            startActivity(intent);
        }
        vunglePub.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
        vunglePub.onPause();
    }


    public void doVungleWay(){
        // get your App ID from the app's main page on the Vungle Dashboard after setting up your app
        final String app_id = "588dde66e38e0e0c1c000366";

        // initialize the Publisher SDK
        vunglePub.init(this, app_id);
    }

    public  void showNetDialogShare(final Context context,String title){

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_view);
        dialog.setTitle(title);

        final TextView editTextKeywordToBlock=(TextView) dialog.findViewById(R.id.editTextKeywordsToBlock);
        TextView ok=(TextView)dialog.findViewById(R.id.ok);
        TextView cancel= (TextView)dialog.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                vunglePub.playAd();
                dialog.dismiss();
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("text/plain");
                //intent.putExtra(Intent.EXTRA_TEXT,postImageItem.getUrl());
                //startActivity(intent);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public  void showNetDialog(final Context context,String title){

        final Dialog dialog = new Dialog(this);

        dialog.setContentView(R.layout.dialog_view_download);
        dialog.setTitle(title);

        final TextView editTextKeywordToBlock=(TextView) dialog.findViewById(R.id.editTextKeywordsToBlock);
        TextView ok=(TextView)dialog.findViewById(R.id.ok);
        TextView cancel= (TextView)dialog.findViewById(R.id.cancel);

        ok.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                vunglePub.playAd();
                dialog.dismiss();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void actionBarTitle(String title){

        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.custom_title_other, null);

        //if you need to customize anything else about the text, do it here.
        //I'm using a custom TextView with a custom font in my layout xml so all I need to do is set title
        ((TextView)v.findViewById(R.id.title)).setText(title);
        //assign the view to the actionbar
        this.getSupportActionBar().setCustomView(v);
    }


}
