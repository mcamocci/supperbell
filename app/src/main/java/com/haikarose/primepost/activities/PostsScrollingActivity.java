package com.haikarose.primepost.activities;

import android.Manifest;
import android.app.SearchManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haikarose.primepost.Pojos.Post;
import com.haikarose.primepost.Pojos.Uploader;
import com.haikarose.primepost.R;
import com.haikarose.primepost.adapters.PostItemAdapter;
import com.haikarose.primepost.tools.CommonInformation;
import com.haikarose.primepost.tools.EndlessRecyclerViewScrollListener;
import com.haikarose.primepost.tools.PermissionHelper;
import com.haikarose.primepost.tools.RetryObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PostsScrollingActivity extends AppCompatActivity implements RetryObject.ReloadListener{

    private List<Object> postListOne=new ArrayList<>();
    private RecyclerView recyclerView;
    private RetryObject retryObject;
    private PostItemAdapter adapter;
    private int uploader_id;
    private int PAGE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posts_scrolling);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Posts");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] PERMISSIONS = {
                Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MEDIA_CONTENT_CONTROL};
        PermissionHelper.check(this,PERMISSIONS);

        uploader_id=getIntent().getIntExtra(Uploader.ID,0);
        //actionBarTitle("Posts");

        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        retryObject= RetryObject.getInstance(this);
        retryObject.setListener(this);

        LinearLayoutManager manager=new LinearLayoutManager(getBaseContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        doTask(CommonInformation.GET_POST_LIST,PAGE,8,postListOne);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                PAGE=page;
                doTask(CommonInformation.GET_POST_LIST,PAGE,totalItemsCount,postListOne);

            }
        });
        adapter=new PostItemAdapter(getBaseContext(),postListOne);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_posts_scrolling,menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(PostsScrollingActivity.this.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();

        ComponentName cn = new ComponentName(this, SearchActivity.class);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(cn));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();

        Intent intent;
        if(id==android.R.id.home){
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
            finish();
        }else if(id==R.id.feedback){
            intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("mailto:")); // only email apps should handle this
            intent.putExtra(Intent.EXTRA_EMAIL,new String[]{"mcamocci@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from Android app user");
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        }else if(id== R.id.rate_us){
            Uri uri = Uri.parse("market://details?id=" + getBaseContext().getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getBaseContext().getPackageName())));
            }

        }else if(id==R.id.about){
            intent=new Intent(PostsScrollingActivity.this,AboutActivity.class);
            startActivity(intent);
        }else if(id==R.id.invites){
            String shared_content="Hello am using "+getResources().getString(R.string.app_name)+" to get recent news posted at IFM. " +
                    "Get it on google play today. https://play.google.com/store/apps/details?id=com.haikarose.mediarose";
            intent=new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,shared_content);
            Intent cooler_one=intent.createChooser(intent,"Complete process with:-");
            startActivity(cooler_one);
        }
        return true;
    }

    public void doTask(final String url, final int page, final int total, final List<Object> categories){

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put(Post.PAGE,page);
        params.put(Post.COUNT,total);
        params.put(Uploader.ID,uploader_id);

        client.post(getBaseContext(), url, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                retryObject.hideMessage();
                retryObject.hideName();
                retryObject.showProgress();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                retryObject.hideMessage();
                retryObject.hideProgress();

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                retryObject.hideProgress();
                retryObject.hideMessage();
                retryObject.hideName();
                Type listType = new TypeToken<List<Post>>() {}.getType();
                List<Post> postList = new Gson().fromJson(responseString, listType);
                categories.addAll(postList);
                adapter.notifyDataSetChanged();

                if(responseString.length()<5 && categories.size()<1){
                    retryObject.hideMessage();
                    retryObject.hideProgress();
                    retryObject.hideName();
                }

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public void onReloaded(String message) {
        doTask(CommonInformation.GET_POST_LIST,PAGE,8,postListOne);

    }

}
