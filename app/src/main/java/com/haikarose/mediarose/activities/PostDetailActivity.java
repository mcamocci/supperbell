package com.haikarose.mediarose.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.haikarose.mediarose.Pojos.Post;
import com.haikarose.mediarose.R;
import com.haikarose.mediarose.adapters.PostImageAdapter;
import com.haikarose.mediarose.tools.CommonInformation;
import com.haikarose.mediarose.tools.ConnectionChecker;
import com.haikarose.mediarose.tools.DateHelper;
import com.haikarose.mediarose.tools.ESAObjectHelper;
import com.haikarose.mediarose.tools.StringUpperHelper;
import com.haikarose.mediarose.tools.TransferrableContent;

import java.util.ArrayList;
import java.util.List;


public class PostDetailActivity extends AppCompatActivity {

    private TextView message;
    private TextView time;
    private  String shared_content;
    private Post post;
    private List<Object> objectsList=new ArrayList<>();
    private PostImageAdapter adapter;
    private RecyclerView resorcesRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        if (!(ConnectionChecker.isInternetConnected(getBaseContext()))) {
            Intent intent=new Intent(getBaseContext(),NoConnectionActivity.class);
            startActivity(intent);
        }

        post= TransferrableContent.fromJsonToPost(getIntent().getStringExtra(Post.EXCHANGE_ID));

        resorcesRecyclerView =(RecyclerView)findViewById(R.id.resources_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        resorcesRecyclerView.setLayoutManager(layoutManager);

        objectsList.addAll(post.getResources());
        adapter=new PostImageAdapter(getBaseContext(),objectsList);
        resorcesRecyclerView.setAdapter(adapter);


        NativeExpressAdView nativeExpressAdView=(NativeExpressAdView)findViewById(R.id.adView);
        //nativeExpressAdView.loadAd(new AdRequest.Builder().build());
        //nativeExpressAdView.setAdUnitId(getResources().getString(R.string.native_ad_unit_id));
        //AdSize size=new AdSize(300,150);
        //nativeExpressAdView.setAdSize(size);
        nativeExpressAdView.loadAd(new AdRequest.Builder().build());


        View view=findViewById(R.id.include);
        ESAObjectHelper.executeEsaProcess(getBaseContext(), CommonInformation.ESA0BJECT_URL,view);


        message=(TextView)findViewById(R.id.message);
        time=(TextView)findViewById(R.id.time_of_update);

        //the things to be shared//
        shared_content=post.getContent()+" , ( Download "
                +getResources().getString(R.string.app_name)+" app. " +
                "https://play.google.com/store/apps/details?id=com.haikarose.mediarose )";

        message.setText(StringUpperHelper.doUpperlization(post.getContent()));

        time.setText(DateHelper.getPresentableDate(post.getDate()));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(StringUpperHelper.doUpperlization(post.getName()));

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

    @Override
    protected void onResume() {
        super.onResume();
        if (!(ConnectionChecker.isInternetConnected(getBaseContext()))) {
            Intent intent=new Intent(getBaseContext(),NoConnectionActivity.class);
            startActivity(intent);
        }
    }
}