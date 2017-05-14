package com.haikarose.primepost;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.haikarose.primepost.Pojos.Uploader;
import com.haikarose.primepost.activities.NoConnectionActivity;
import com.haikarose.primepost.adapters.UploaderItemAdapter;
import com.haikarose.primepost.tools.CommonInformation;
import com.haikarose.primepost.tools.ConnectionChecker;
import com.haikarose.primepost.tools.RetryObjectFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class HomeFragment extends  Fragment {

    private List<Object> uploaderList=new ArrayList<>();
    private RecyclerView recyclerView;
    private RetryObjectFragment retryObject;
    private UploaderItemAdapter adapter;


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
        retryObject= RetryObjectFragment.getInstance(view);


        recyclerView=(RecyclerView)view.findViewById(R.id.recycler_view);

        LinearLayoutManager manager=new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);

        doTask(CommonInformation.GET_POST_UPLOADERS,uploaderList);

       /* recyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(manager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                doTask(CommonInformation.GET_POST_UPLOADERS,uploaderList);

            }
        });*/
        adapter=new UploaderItemAdapter(getContext(),uploaderList);
        recyclerView.setAdapter(adapter);

        //the network issue is solved here
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void doTask(final String url,final List<Object> categories){

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params=new RequestParams();

        client.post(getContext(), url, params, new TextHttpResponseHandler() {

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
                if(categories.size()<1){
                    retryObject.showName();
                    retryObject.showMessage();
                }
                retryObject.setListener(new RetryObjectFragment.ReloadListener() {
                    @Override
                    public void onReloaded(String message) {
                        doTask(url,categories);
                    }
                });

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                retryObject.hideProgress();
                retryObject.hideMessage();
                retryObject.hideName();
                Type listType = new TypeToken<List<Uploader>>() {}.getType();
                List<Uploader> postersList = new Gson().fromJson(responseString, listType);
                categories.addAll(postersList);
                adapter.notifyDataSetChanged();

                if(responseString.length()<5 && categories.size()<1){
                    retryObject.setReason("There are no content!");
                    retryObject.hideMessage();
                    retryObject.hideProgress();
                    retryObject.hideName();
                    retryObject.showMessage();

                }


            }
        });
    }

}

