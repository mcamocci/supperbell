package com.haikarose.mediarose;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haikarose.mediarose.Pojos.Post;
import com.haikarose.mediarose.activities.MainActivity;
import com.haikarose.mediarose.activities.NoConnectionActivity;
import com.haikarose.mediarose.activities.SearchActivity;
import com.haikarose.mediarose.adapters.PostItemAdapter;
import com.haikarose.mediarose.tools.CommonInformation;
import com.haikarose.mediarose.tools.ConnectionChecker;
import com.haikarose.mediarose.tools.EndlessRecyclerViewScrollListener;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class HomeFragment extends  Fragment {

    private View retry_view;
    private List<Object> postListOne=new ArrayList<>();
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private PostItemAdapter adapter;


    public HomeFragment () {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!(ConnectionChecker.isInternetConnected(getContext()))) {
            Intent intent=new Intent(getContext(),NoConnectionActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_home, container, false);

        swipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        retry_view=(View)view.findViewById(R.id.retry);
        retry_view.setVisibility(View.GONE);


        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);

        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        doTask(CommonInformation.GET_POST_LIST,0,8,postListOne);

        recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                doTask(CommonInformation.GET_POST_LIST,page,totalItemsCount,postListOne);

            }
        });
        adapter=new PostItemAdapter(getContext(),postListOne);
        recyclerView.setAdapter(adapter);

        //the network issue is solved here
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void doTask(final String url, final int page, final int total, final List<Object> categories){

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();
        params.put(Post.PAGE,page);
        params.put(Post.COUNT,total);

        client.post(getContext(), url, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                swipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                swipeRefreshLayout.setRefreshing(false);
                retry_view.setVisibility(View.VISIBLE);

                Toast.makeText(getContext(),responseString,Toast.LENGTH_SHORT).show();

                retry_view.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View v) {
                        retry_view.setVisibility(View.VISIBLE);
                        TextView textView=(TextView)retry_view.findViewById(R.id.message_text);
                        textView.setText(R.string.no_connection);
                        doTask(url,page,total,categories);

                    }

                });
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                retry_view.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                Type listType = new TypeToken<List<Post>>() {}.getType();
                List<Post> postList = new Gson().fromJson(responseString, listType);
                categories.addAll(postList);
                adapter.notifyDataSetChanged();

                if(responseString.length()<5 && categories.size()<1){

                    retry_view.setVisibility(View.VISIBLE);
                    TextView textView=(TextView)retry_view.findViewById(R.id.message_text);
                    textView.setText(R.string.no_content);
                    retry_view.setOnClickListener(new View.OnClickListener(){

                        @Override
                        public void onClick(View v) {
                            doTask(url,page,total,categories);
                        }

                    });
                }


            }
        });
    }

}

