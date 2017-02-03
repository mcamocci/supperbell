package com.haikarose.mediarose.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haikarose.mediarose.Pojos.Post;
import com.haikarose.mediarose.R;
import com.haikarose.mediarose.adapters.PostItemAdapter;
import com.haikarose.mediarose.tools.CommonInformation;
import com.haikarose.mediarose.tools.ConnectionChecker;
import com.haikarose.mediarose.tools.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class SearchActivity extends AppCompatActivity {

    private String query="";
    private SearchView searchView;
    private View retry_view;
    private List<Object> postListOne=new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PostItemAdapter adapter;
    private String keyword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handleIntent(getIntent());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        retry_view=(View)findViewById(R.id.retry);
        retry_view.setVisibility(View.GONE);


        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);

        LinearLayoutManager manager=new LinearLayoutManager(getBaseContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);


        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                doTask(CommonInformation.GET_POST_LIST_SEARCH,0,8,keyword,postListOne);

            }
        });
        adapter=new PostItemAdapter(getBaseContext(),postListOne);
        recyclerView.setAdapter(adapter);



        if (!(ConnectionChecker.isInternetConnected(getBaseContext()))) {
            Intent intent=new Intent(getBaseContext(),NoConnectionActivity.class);
            startActivity(intent);
        }

    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            query =
                    intent.getStringExtra(SearchManager.QUERY);
            if(searchView!=null){
                searchView.setQuery(query,true);
            }
            doSearch(query);
        }
    }

    private void doSearch(String queryStr) {
        Toast.makeText(getBaseContext(),getResources().getString(R.string.search_message)+" "+queryStr,Toast.LENGTH_SHORT).show();
        keyword=queryStr;
        doTask(CommonInformation.GET_POST_LIST_SEARCH,0,8,queryStr,postListOne);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if(id==android.R.id.home){
            finish();
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

    public void doTask(final String url, final int page, final int total,final String keyword,final List<Object> categories){

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put(Post.PAGE,page);
        params.put(Post.COUNT,total);
        params.put(Post.SEARCH,keyword);

        client.post(getBaseContext(), url, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                retry_view.setVisibility(View.VISIBLE);

                retry_view.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {

                        retry_view.setVisibility(View.VISIBLE);
                        TextView textView=(TextView)retry_view.findViewById(R.id.message_text);
                        textView.setText(R.string.no_connection);
                        doTask(url,page,total,keyword,categories);

                    }

                });
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                Log.e("content loaded",responseString);
                retry_view.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);


                if(responseString.length()<5 && categories.size()<1){

                    retry_view.setVisibility(View.VISIBLE);
                    TextView textView=(TextView)retry_view.findViewById(R.id.message_text);
                    textView.setText(R.string.no_content);
                    retry_view.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            doTask(url,page,total,keyword,categories);
                        }

                    });
                }else{
                    Type listType = new TypeToken<List<Post>>() {}.getType();
                    List<Post> postList = new Gson().fromJson(responseString, listType);
                    categories.addAll(postList);
                    adapter.notifyDataSetChanged();
                }


            }
        });
    }

}