package com.haikarose.mediarose.activities;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haikarose.mediarose.Pojos.Post;
import com.haikarose.mediarose.R;
import com.haikarose.mediarose.adapters.PostItemAdapter;
import com.haikarose.mediarose.tools.CommonInformation;
import com.haikarose.mediarose.tools.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {

    private List<Object> postList=new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PostItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        LinearLayoutManager manager=new LinearLayoutManager(getBaseContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);


        //the fake data
        fakeData();

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                doTask(CommonInformation.GET_POST_LIST,page,8,postList);

            }
        });

        adapter=new PostItemAdapter(getBaseContext(),postList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(MainActivity.this.SEARCH_SERVICE);
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
            finish();
        }else if(id==R.id.about){
            intent=new Intent(MainActivity.this,AboutActivity.class);
            startActivity(intent);
        }else if(id==R.id.report){
            intent=new Intent(MainActivity.this,ReportProblem.class);
            startActivity(intent);
        }else if(id==R.id.invites){
            String shared_content="Hello am using "+getResources().getString(R.string.app_name)+" to get recent news of my favorite channel. " +
                    "Get it on google play today. https://play.google.com/store/apps/details?id=superbell.haikarose.com";
            intent=new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT,shared_content);
            Intent cooler_one=intent.createChooser(intent,"Complete process with:-");
            startActivity(cooler_one);
        }
        return true;
    }


    public void fakeData(){

        for(int i=0;i<10;i++){
            Post post=new Post();

            if(i==2 || i==7){
                post.setResource("https://scontent-bru2-1.xx.fbcdn.net/v/t1.0-9/16195034_760896287395877_1216980308058827388_n.jpg?_nc_eui2=v1%3AAeEeVhV4-89wpti2Qi5yaJsvu2XzIT6shc3RTzXkXXDy_Thk8pmzWvBf4xEnwsBHLmoiptK1Wycnx9HBViapuDq2O3Y31Ch22yDffkseEfRTKw&oh=d835e32fb126bb13229fd01de2d63bba&oe=58FEF358");
            }else{
                post.setResource(null);
            }

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm");
            Date date=new Date();
            String time=sdf.format(date);
            post.setDate(time);

            post.setMesage("It could be that you have only recently created a new Ad Unit ID and requesting for live ads. It could take a few hours for ads to start getting served if that is that case");
            postList.add(post);
        }

    }

    public void doTask(String url, final int page, int total, final List<Object> categories){

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();

        try{
            url+= URLEncoder.encode(Integer.toString(page),"UTF-8")+"/"+URLEncoder.encode(Integer.toString(total),"UTF-8");
        }catch (Exception ex){

        }


        client.get(getBaseContext(), url, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Toast.makeText(getBaseContext(),responseString,Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                swipeRefreshLayout.setRefreshing(false);
                Type listType = new TypeToken<List<Post>>() {}.getType();
                List<Post> yourList = new Gson().fromJson(responseString, listType);
                //addNativeAddToList(page,categories);
                categories.addAll(yourList);
                adapter.notifyDataSetChanged();

            }
        });
    }
}
