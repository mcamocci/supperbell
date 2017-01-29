package com.haikarose.mediarose.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.haikarose.mediarose.R;

public class SearchActivity extends AppCompatActivity {

    private String query="";
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        handleIntent(getIntent());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        Toast.makeText(getBaseContext(),queryStr,Toast.LENGTH_SHORT).show();
        // get a Cursor, prepare the ListAdapter
        // and set it
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
}