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

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.NativeExpressAdView;
import com.haikarose.mediarose.Pojos.Post;
import com.haikarose.mediarose.Pojos.PostImageItem;
import com.haikarose.mediarose.R;
import com.haikarose.mediarose.adapters.PostImageAdapter;
import com.haikarose.mediarose.tools.CommonInformation;
import com.haikarose.mediarose.tools.ESAObjectHelper;
import com.haikarose.mediarose.tools.StringUpperHelper;

import java.util.ArrayList;
import java.util.List;


public class PostDetailActivity extends AppCompatActivity {

    private TextView message;
    private TextView time;
    private  String shared_content;
    private Post post;
    private PostImageAdapter adapter;
    private List<Object> listOfPostImageItem;
    private RecyclerView resorcesRecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        PostImageItem item1=new PostImageItem();
                item1.setUrl("https://scontent-bru2-1.xx.fbcdn.net/v/t1.0-9/16427591_165065320653762_2016485798572253102_n.jpg?_nc_eui2=v1%3AAeEQ8Fvr2urGmqVBXKEI3-E6275JWQnxbxeVTL9gsUwAgDNUs3aSyDmuTHbOKvWF_2Elzs8FGYgmC_x73Bro_xNX3V9ofzXOsej2EY1I71CWhA&oh=08b9fb3c3a18e3827e975094d2e97c7f&oe=59175356");
        PostImageItem item2=new PostImageItem();
                item2.setUrl("https://scontent-bru2-1.xx.fbcdn.net/v/t1.0-9/16195034_760896287395877_1216980308058827388_n.jpg?_nc_eui2=v1%3AAeEeVhV4-89wpti2Qi5yaJsvu2XzIT6shc3RTzXkXXDy_Thk8pmzWvBf4xEnwsBHLmoiptK1Wycnx9HBViapuDq2O3Y31Ch22yDffkseEfRTKw&oh=d835e32fb126bb13229fd01de2d63bba&oe=58FEF358");
        PostImageItem item3=new PostImageItem();
                item3.setUrl("https://scontent-bru2-1.xx.fbcdn.net/v/t1.0-9/15319223_1279994318739746_199017675508630965_n.jpg?_nc_eui2=v1%3AAeGg8_ic4UDOcO6TnxG_3iImSi5j8c6i0OzyhmixZTIQixLuClQHin7E8_fADWp2adFRDxtd-MsqdXgB6Plw7IkDF7InKFAAWYDiffh1XdBm6g&oh=44ecc2410f2fe5f7778618028baac31c&oe=58FEA8BF");


        listOfPostImageItem=new ArrayList<>();
        listOfPostImageItem.add(item1);
        listOfPostImageItem.add(item2);
        listOfPostImageItem.add(item3);

        resorcesRecyclerView =(RecyclerView)findViewById(R.id.resources_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getBaseContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        resorcesRecyclerView.setLayoutManager(layoutManager);
        adapter=new PostImageAdapter(getBaseContext(),listOfPostImageItem);
        resorcesRecyclerView.setAdapter(adapter);


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

        //the things to be shared//
        shared_content=post.getMesage()+" , ( Why waiting for a friend to share? please download mediabell app. " +
                "https://play.google.com/store/apps/details?id=mediabells.meena.com.mediabells16 )";

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