package com.example.jonathan.moviedatabase_stage1;

import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.jonathan.moviedatabase_stage1.Data.MovieDataModel;
import com.example.jonathan.moviedatabase_stage1.Utils.JsonUtils;
import com.example.jonathan.moviedatabase_stage1.Utils.NetworkUtils;
import com.example.jonathan.moviedatabase_stage1.Utils.PosterAdapter;
import com.example.jonathan.moviedatabase_stage1.Utils.PosterLayoutSize;
import com.example.jonathan.moviedatabase_stage1.Utils.SettingsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements PosterAdapter.OnItemClicked, AdapterView.OnItemSelectedListener {

    private static RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;
    private static ArrayList<MovieDataModel> data;
    public static View.OnClickListener posterOnClickListener;

    private TextView mResultsTextView;

    //Json Object for holding movie data from server
    private JSONObject baseJsonResult;
    private JSONArray MovieDatabase;

    //Dynamic Grid Layout
    private PosterLayoutSize posterLayoutSize;


    //Movie Filter
    public Map<String, String> filterBy = new LinkedHashMap<String, String>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SetupToolbar
        Toolbar toolbar = findViewById(R.id.movie_menu);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setTitle(R.string.app_name);

        //Dim UI
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //SetupHashMap
        filterBy.put("Popularity","popular");
        filterBy.put("Rating","top_rating");

        //Debug
        mResultsTextView = findViewById(R.id.movie_list_tv);

        //posterOnClickListener = new PosterOnClickListener();

        recyclerView = findViewById(R.id.movie_poster_rv);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Get JSON Data
        makeTMDBQuery("top_rated");

        //Get Layout Size
        posterLayoutSize = new PosterLayoutSize();

        data = new ArrayList<>();

        if ( MovieDatabase != null) {
            for (int i = 0; i < 10; i++) {
                try {
                    data.add(JsonUtils.parseMovieData(MovieDatabase.getJSONObject(i)));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if( data != null) {
            adapter = new PosterAdapter(data,posterLayoutSize.getParams());
        }

        recyclerView.setAdapter(adapter);
    }


    private void makeTMDBQuery(String query) {
        String databaseQuery = query;
        URL searchURL = NetworkUtils.buildURI(databaseQuery);

        new TheMovieDatabaseQuery().execute(searchURL);

    }

    private class TheMovieDatabaseQuery extends AsyncTask<URL,Void,String> {

        @Override
        protected String doInBackground(URL... params) {
            URL searchUrl = params[0];
            String databaseQuery = null;

            try {
                databaseQuery = NetworkUtils.getResponseHTTP(searchUrl);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return databaseQuery;
        }


        @Override
        protected void onPostExecute(String databaseQuery) {
            if(databaseQuery != null ) {
                //Set Text view to results
                mResultsTextView.setText(databaseQuery);
                try {
                    baseJsonResult = new JSONObject(databaseQuery);
                    MovieDatabase = baseJsonResult.getJSONArray("results");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            } else {
                mResultsTextView.setText(R.string.Error_Data);
            }


            //populate data and refresh adapter
            //Clear data
            data.clear();


            //if ( MovieDatabase != null) {
                for (int i = 0; i < MovieDatabase.length(); i++) {
                    try {
                        data.add(JsonUtils.parseMovieData(MovieDatabase.getJSONObject(i)));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            //}

            adapter = new PosterAdapter(data, posterLayoutSize.getParams());
            //recyclerView.setAdapter(adapter);
            recyclerView.swapAdapter(adapter, true);
        }


    }

    @Override
    public void onItemClick() {
        //intent code here
        Log.d("VIEW:", "hello world");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.movie_menu, menu);

        MenuItem item = menu.findItem(R.id.filter_spinner);
        Spinner spinner = (Spinner) item.getActionView();

        // Use Custom Data Class
        List<SpinnerValues> spinnerValues = new ArrayList<SpinnerValues>();
        String[] keyArray = getResources().getStringArray(R.array.movie_filter_names);
        String[] valueArray = getResources().getStringArray(R.array.movie_filter_values);
        for(int i = 0; i < keyArray.length; i++) {
            spinnerValues.add(new SpinnerValues(keyArray[i], valueArray[i]));
        }

        /*
        ArrayAdapter<CharSequence> spin_adapter = ArrayAdapter
                .createFromResource(this,
                        R.array.movie_filter_values,
                        android.R.layout.simple_spinner_dropdown_item);
        spin_adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        */
        ArrayAdapter<SpinnerValues> spin_adapter = new ArrayAdapter<SpinnerValues>(this, android.R.layout.simple_spinner_item, spinnerValues);
        spin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spin_adapter);
        spinner.setOnItemSelectedListener(this);
        return true;
    }

    public void onItemSelected(AdapterView<?> parent,
                               View view,
                               int pos,
                               long id) {
        Log.d("VIEW: ","spinner reached");
        Log.d("VIEW: ", parent.getItemAtPosition(pos).toString());
        makeTMDBQuery(parent.getItemAtPosition(pos).toString());
        SpinnerValues sv = (SpinnerValues) parent.getItemAtPosition(pos);
        makeTMDBQuery(sv.getKey());
    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Do Nothing
    }

    private class SpinnerValues {
        public String value;
        public String key;

        public SpinnerValues(String value, String key) {
            this.value = value;
            this.key = key;
        }

        @Override
        public String toString() {
            return value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
