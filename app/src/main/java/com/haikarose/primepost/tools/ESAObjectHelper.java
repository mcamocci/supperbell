package com.haikarose.primepost.tools;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.haikarose.primepost.Pojos.ESAObject;
import com.haikarose.primepost.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by root on 8/31/16.
 */
public class ESAObjectHelper {

    public static void executeEsaProcess(final Context context, final String url, final View view){

        AsyncHttpClient client=new AsyncHttpClient();
        RequestParams params = new RequestParams();

        client.get(context,url, params, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                view.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

                if (responseString.length()>6){

                    Log.e("the response string",responseString);
                    ESAObject object=new Gson().fromJson(responseString,ESAObject.class);

                    TextView brand=(TextView)view.findViewById(R.id.brand_name);

                    TextView message=(TextView)view.findViewById(R.id.promo_message);

                    ImageView promo_image=(ImageView)view.findViewById(R.id.promo_image);

                    String promo_image_url=object.getPromo_image();

                    brand.setText(object.getBrand_name());

                    message.setText(object.getPromo_message());

                    Glide.with(context).load(promo_image_url).
                            fitCenter().centerCrop().into(promo_image);

                    view.setVisibility(View.VISIBLE);

                }else{

                    view.setVisibility(View.GONE);

                }

            }

        });
    }
}
