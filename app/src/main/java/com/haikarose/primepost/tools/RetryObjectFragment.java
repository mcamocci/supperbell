package com.haikarose.primepost.tools;

import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.haikarose.primepost.R;

/**
 * Created by root on 5/6/17.
 */

public class RetryObjectFragment {

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getMessage() {
        return message;
    }

    public void setMessage(TextView message) {
        this.message = message;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public void setProgressBar(ProgressBar progressBar) {
        this.progressBar = progressBar;
    }

    public void hideProgress(){
        this.progressBar.setVisibility(View.INVISIBLE);
    }
    public void showProgress(){
        this.progressBar.setVisibility(View.VISIBLE);
    }

    public void hideMessage(){
        this.message.setVisibility(View.INVISIBLE);
    }

    public  void showMessage(){
        this.message.setVisibility(View.VISIBLE);
    }

    public void hideName(){
        this.name.setVisibility(View.INVISIBLE);
    }
    public void showName(){
        this.name.setVisibility(View.VISIBLE);
    }

    public void setReason(String reason){
        this.message.setText(reason);
    }

    private TextView name;
    private TextView message;
    private ProgressBar progressBar;
    private ReloadListener listener;




    public void setListener(ReloadListener listener){
        this.listener=listener;
    }

    public ReloadListener getListener(){
        return this.listener;
    }

    public static RetryObjectFragment getInstance(View view){

        TextView name=(TextView)view.findViewById(R.id.retryLabel);
        TextView message=(TextView)view.findViewById(R.id.message);
        ProgressBar progressBar=(ProgressBar)view.findViewById(R.id.progressBar);

        final RetryObjectFragment object=new RetryObjectFragment();
        object.setMessage(message);
        object.setName(name);
        object.setProgressBar(progressBar);
        object.getName().setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                object.getListener().onReloaded("reloaded");
            }
        });

        return object;
    }


    public interface ReloadListener{
        void onReloaded(String message);
    }


}
