package com.haikarose.mediarose.activities;

import android.app.DownloadManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.google.android.gms.common.api.GoogleApiClient;
import com.haikarose.mediarose.Pojos.Post;
import com.haikarose.mediarose.R;
import com.vungle.publisher.VunglePub;
import java.net.MalformedURLException;
import java.net.URL;

public class ImageViewerActivity extends AppCompatActivity implements RewardedVideoAdListener {

    // get the VunglePub instance
    final VunglePub vunglePub = VunglePub.getInstance();

    DownloadManager downloadManager;
    private RewardedVideoAd rewardedVideoAd;
    private Post post;
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);

        rewardedVideoAd= MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        doVungleWay();
        loadAd();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.photo_viewer));
        getSupportActionBar().setIcon(R.drawable.ic_action_camera);
        post= Post.postFromBundle(getIntent().getExtras());

        ImageView promo_image = (ImageView) findViewById(R.id.imageView2);
        Context context = getBaseContext();

        promo_image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

                if(vunglePub.isAdPlayable()){
                    vunglePub.playAd();
                }

                /*if(rewardedVideoAd.isLoaded()){
                    rewardedVideoAd.show();
                }*/
            }
        });

        URL url1 = null;
        try {
            url1 = new URL(post.getResource());
            //url=new URL("https://scontent.xx.fbcdn.net/v/t1.0-0/s480x480/14729116_559960550855632_879800026931900081_n.jpg?_nc_eui2=v1%3AAeEwBUzYNejWJ0xA5CO0ceyXps7kzBdw48Px2E8-l2JSYV1FjC6Ic1CdSmjr1qujuDXN9DvNTuGR1cwpEkIyqDt_PkjSlxTWDN_ICk8l0R-nPQ&oh=f65d0c50e6a01416f770d7d2756b64b0&oe=58954080");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Glide.with(context).load(url1).placeholder(android.R.drawable.editbox_dropdown_light_frame).into(promo_image);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.app_gallery_menu, menu);
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



}
