package com.haikarose.mediarose.activities;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.haikarose.mediarose.Pojos.PostImageItem;
import com.haikarose.mediarose.R;
import com.haikarose.mediarose.tools.ConnectionChecker;
import com.haikarose.mediarose.tools.TransferrableContent;
import com.vungle.publisher.VunglePub;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageViewerActivity extends AppCompatActivity implements RewardedVideoAdListener {

    // get the VunglePub instance
    final VunglePub vunglePub = VunglePub.getInstance();

    DownloadManager downloadManager;
    private RewardedVideoAd rewardedVideoAd;
    private PostImageItem postImageItem;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        if (!(ConnectionChecker.isInternetConnected(getBaseContext()))) {
            Intent intent=new Intent(getBaseContext(),NoConnectionActivity.class);
            startActivity(intent);
        }
        rewardedVideoAd= MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        doVungleWay();
        loadAd();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.photo_viewer));
        getSupportActionBar().setIcon(R.drawable.ic_action_camera);
        postImageItem= TransferrableContent.fromJsonToPostImageItem(getIntent().getStringExtra(PostImageItem.EXCHANGE_RES_ID));

        ImageView promo_image = (ImageView) findViewById(R.id.imageView2);
        Context context = getBaseContext();


        URL url1 = null;
        try {
            url1 = new URL(postImageItem.getUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Glide.with(context).load(url1).placeholder(android.R.drawable.editbox_dropdown_light_frame).into(promo_image);

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
        }else if(id==R.id.share){
            if(vunglePub.isAdPlayable()){
                showNetDialog(getBaseContext(),"Share content");
            }else{

            }
        }else if(id==R.id.download){
            showNetDialog(getBaseContext(),"Download content");
            if(vunglePub.isAdPlayable()){
                showNetDialog(getBaseContext(),"Download content");
            }else{

            }
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
        vunglePub.onPause();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    @Override
    public void onRewardedVideoAdClosed() {

    }

    public void loadAd(){
        if(!rewardedVideoAd.isLoaded()){
            //ca-app-pub-2816298192313243/6307470213
            //rewardedVideoAd.loadAd(AdRequest.Builder.addTestDevice("89CA7DE86D7FEE4D3EDAAE6157B5CBFB");
            rewardedVideoAd.loadAd("89CA7DE86D7FEE4D3EDAAE6157B5CBFB",new AdRequest.Builder().build());
        }
    }

    public void doVungleWay(){
        // get your App ID from the app's main page on the Vungle Dashboard after setting up your app
        final String app_id = "588dde66e38e0e0c1c000366";

        // initialize the Publisher SDK
        vunglePub.init(this, app_id);
    }

    public  void showNetDialog(final Context context,String title){

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
            }
        });
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();


      /*  final AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setTitle(title);
        adb.setMessage(message);

        //Set the Yes/Positive and No/Negative Button text
        String yesButtonText = "Yes";
        String noButtonText = "No";
        //Define the positive button text and action on alert dialog
        adb.setPositiveButton(yesButtonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which){
                //something to be done here
                *//*Intent intent=new Intent(context, SendingLogIntentService.class);
                context.startService(intent);*//*

            }
        });

        //Define the negative button text and action on alert dialog
        adb.setNegativeButton(noButtonText, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which){
                //something else to be also done here
            }
        });

        //Display the Alert Dialog on app interface
        adb.show();*/
    }



}
