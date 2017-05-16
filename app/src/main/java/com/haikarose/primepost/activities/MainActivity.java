package com.haikarose.primepost.activities;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.haikarose.primepost.HomeFragment;
import com.haikarose.primepost.R;
import com.haikarose.primepost.tools.ConnectionChecker;
import com.haikarose.primepost.tools.PermissionHelper;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarTitle("PrimePost");

        if (!(ConnectionChecker.isInternetConnected(getBaseContext()))) {
            Intent intent=new Intent(getBaseContext(),NoConnectionActivity.class);
            startActivity(intent);
        }

        HomeFragment homeFragment=new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.MEDIA_CONTENT_CONTROL};
        PermissionHelper.check(this,PERMISSIONS);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);
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
            intent=new Intent(MainActivity.this,AboutActivity.class);
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



    @Override
    protected void onResume() {
        super.onResume();
        if (!(ConnectionChecker.isInternetConnected(getBaseContext()))) {
            Intent intent=new Intent(getBaseContext(),NoConnectionActivity.class);
            startActivity(intent);
        }
    }

    public void actionBarTitle(String title){

        this.getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LayoutInflater inflator = LayoutInflater.from(this);
        View v = inflator.inflate(R.layout.custom_title_other, null);

        //if you need to customize anything else about the text, do it here.
        //I'm using a custom TextView with a custom font in my layout xml so all I need to do is set title
        ((TextView)v.findViewById(R.id.title)).setText(title);
        //assign the view to the actionbar
        this.getSupportActionBar().setCustomView(v);
    }


}
