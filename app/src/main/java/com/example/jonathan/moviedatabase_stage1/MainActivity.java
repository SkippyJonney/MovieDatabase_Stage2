package com.example.jonathan.moviedatabase_stage1;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.jonathan.moviedatabase_stage1.Data.MovieDataModel;
import com.example.jonathan.moviedatabase_stage1.Data.sampleMovieData;
import com.example.jonathan.moviedatabase_stage1.Utils.JsonUtils;
import com.example.jonathan.moviedatabase_stage1.Utils.NetworkUtils;
import com.example.jonathan.moviedatabase_stage1.Utils.PosterAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridManager;
    private static RecyclerView recyclerView;
    private static ArrayList<MovieDataModel> data;
    public static View.OnClickListener posterOnClickListener;

    private TextView mResultsTextView;

    //Json Object for holding movie data from server
    private JSONObject baseJsonResult;
    private JSONArray MovieDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mResultsTextView = findViewById(R.id.movie_list_tv);

        posterOnClickListener = new PosterOnClickListener(this);

        recyclerView = findViewById(R.id.movie_poster_rv);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        gridManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(gridManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        //Get JSON Data
        makeTMDBQuery();


        data = new ArrayList<MovieDataModel>();
        for(int i = 0; i < sampleMovieData.Length(); i++) {
                //data.add(JsonUtils.parseMovieData(MovieDatabase.getJSONObject(i)));
            MovieDataModel newMovie = new MovieDataModel();
            newMovie.setOriginalTitle(sampleMovieData.getString(i));
            data.add(newMovie);
        }

        if ( MovieDatabase != null) {
            for (int i = 0; i < 10; i++) {
                try {
                    data.add(JsonUtils.parseMovieData(MovieDatabase.getJSONObject(i)));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }

        adapter = new PosterAdapter(data);
        recyclerView.setAdapter(adapter);

    }


    private static class PosterOnClickListener implements View.OnClickListener {

        private final Context context;

        private PosterOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            LaunchDetails(v);
        }

        private void LaunchDetails(View v) {
            Log.d("VIEW:", v.toString());
        }
    }

    private void makeTMDBQuery() {
        String databaseQuery = "popular";
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
                mResultsTextView.setText("Data Not Found");
            }


            //populate data and refresh adapter

            //if ( MovieDatabase != null) {
                for (int i = 0; i < MovieDatabase.length(); i++) {
                    try {
                        data.add(JsonUtils.parseMovieData(MovieDatabase.getJSONObject(i)));
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            //}

            adapter = new PosterAdapter(data);
            recyclerView.setAdapter(adapter);
        }


    }
}
