package com.haikarose.mediarose.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.haikarose.mediarose.Pojos.Post;
import com.haikarose.mediarose.R;
import com.haikarose.mediarose.tools.CommonInformation;
import com.haikarose.mediarose.tools.ESAObjectHelper;
import com.haikarose.mediarose.tools.StringUpperHelper;


public class PostDetailActivity extends AppCompatActivity {

    private TextView message;
    private TextView time;
    private RecyclerView update_items_recycler_view;
    private  String shared_content;
    private Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        NativeExpressAdView nativeExpressAdView=(NativeExpressAdView)findViewById(R.id.adView);
        //nativeExpressAdView.loadAd(new AdRequest.Builder().build());
        //nativeExpressAdView.setAdUnitId(getResources().getString(R.string.native_ad_unit_id));
        //AdSize size=new AdSize(300,150);
        //nativeExpressAdView.setAdSize(size);
        nativeExpressAdView.loadAd(new AdRequest.Builder().build());


        post=Post.postFromBundle(getIntent().getExtras());

        View view=findViewById(R.id.include);
        ESAObjectHelper.executeEsaProcess(getBaseContext(), CommonInformation.ESA0BJECT_URL,view);


        message=(TextView)findViewById(R.id.message);
        time=(TextView)findViewById(R.id.time_of_update);
        update_items_recycler_view=(RecyclerView)findViewById(R.id.resources_recycler_view);
        update_items_recycler_view.setItemAnimator(new DefaultItemAnimator());

        //the things to be shared//
        shared_content=post.getMesage()+" , ( Why waiting for a friend to share? please download mediabell app. " +
                "https://play.google.com/store/apps/details?id=mediabells.meena.com.mediabells16 )";

        LinearLayoutManager layoutManager=new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        update_items_recycler_view.setLayoutManager(layoutManager);

        message.setText(StringUpperHelper.doUpperlization(post.getMesage()));


        time.setText(post.getDate());

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(StringUpperHelper.doUpperlization(getResources().getString(R.string.app_name)));




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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.post_detail_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        if(id==android.R.id.home){

            finish();

        }else if(id==R.id.share){

            //do the sharing here//

            Intent intent=new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,shared_content);

            //Intent cooler_one=intent.createChooser(intent,"Invites friends to use media bell");
            startActivity(intent);

        }

        return true;
    }
}