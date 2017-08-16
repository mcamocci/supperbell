package com.haikarose.primepost.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
import com.haikarose.primepost.tools.RetryObject;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class PostActivity extends AppCompatActivity implements RetryObject.ReloadListener {

    private List<Object> postListOne = new ArrayList<>();
    private RecyclerView recyclerView;
    private RetryObject retryObject;
    private PostItemAdapter adapter;
    private int uploader_id;
    private int PAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        uploader_id = getIntent().getIntExtra(Uploader.ID, 0);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarTitle("Posts");

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        retryObject = RetryObject.getInstance(this);
        retryObject.setListener(this);

        LinearLayoutManager manager = new LinearLayoutManager(getBaseContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        recyclerView.setNestedScrollingEnabled(false);

        doTask(CommonInformation.GET_POST_LIST, PAGE, 8, postListOne);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                PAGE = page;
                doTask(CommonInformation.GET_POST_LIST, PAGE, totalItemsCount, postListOne);

            }
        });
        adapter = new PostItemAdapter(getBaseContext(), postListOne);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public void actionBarTitle(String title) {

        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.custom_title_other, null);

        //if you need to customize anything else about the text, do it here.
        //I'm using a custom TextView with a custom font in my layout xml so all I need to do is set title
        ((TextView) v.findViewById(R.id.title)).setText(title);
        //assign the view to the actionbar
        this.getSupportActionBar().setCustomView(v);
    }

    public void doTask(final String url, final int page, final int total, final List<Object> categories) {

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(Post.PAGE, page);
        params.put(Post.COUNT, total);
        params.put(Uploader.ID, uploader_id);

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
                Type listType = new TypeToken<List<Post>>() {
                }.getType();
                List<Post> postList = new Gson().fromJson(responseString, listType);
                categories.addAll(postList);
                adapter.notifyDataSetChanged();

                if (responseString.length() < 5 && categories.size() < 1) {
                    retryObject.hideMessage();
                    retryObject.hideProgress();
                    retryObject.hideName();
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }
        return true;
    }

    @Override
    public void onReloaded(String message) {
        doTask(CommonInformation.GET_POST_LIST, PAGE, 8, postListOne);
    }
}
