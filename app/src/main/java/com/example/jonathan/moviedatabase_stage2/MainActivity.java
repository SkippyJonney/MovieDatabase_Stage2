package com.example.jonathan.moviedatabase_stage2;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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

import com.example.jonathan.moviedatabase_stage2.Data.MovieContract;
import com.example.jonathan.moviedatabase_stage2.Data.MovieDataModel;
import com.example.jonathan.moviedatabase_stage2.Utils.JsonUtils;
import com.example.jonathan.moviedatabase_stage2.Utils.NetworkUtils;
import com.example.jonathan.moviedatabase_stage2.Utils.PosterAdapter;
import com.example.jonathan.moviedatabase_stage2.Utils.PosterLayoutSize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements PosterAdapter.OnItemClicked, AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static RecyclerView.Adapter adapter;
    private RecyclerView recyclerView;

    /* Cursor Adapter */
    private PosterAdapter mAdapter;
    // const for unique loader
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    private Bundle mCursorArgs = new Bundle();
    private String currentFilterKey;

    //private static ArrayList<MovieDataModel> data;
    //public static View.OnClickListener posterOnClickListener;

    //private TextView mResultsTextView;

    private JSONArray MovieDatabase;

    //Dynamic Grid Layout
    private PosterLayoutSize posterLayoutSize;


    //Movie Filter
    private final Map<String, String> filterBy = new LinkedHashMap<>();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //SetupToolbar
        Toolbar toolbar = findViewById(R.id.movie_menu);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        try {
            Objects.requireNonNull(ab).setDisplayShowHomeEnabled(true);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        Objects.requireNonNull(ab).setTitle(R.string.app_name);

        //Dim UI
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        //SetupHashMap
        filterBy.put("Popularity","popular");
        filterBy.put("Rating","top_rating");


        //Debug
        //mResultsTextView = findViewById(R.id.movie_list_tv);

        //posterOnClickListener = new PosterOnClickListener();

        recyclerView = findViewById(R.id.movie_poster_rv);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridManager = new GridLayoutManager(this, 2);

        recyclerView.setLayoutManager(gridManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Get JSON Data
        currentFilterKey = "top_rated";
        makeTMDBQuery("top_rated");
        //mCursorArgs = new Bundle();
        //mCursorArgs.putString("query","top_rated");


        //Get Layout Size
        posterLayoutSize = new PosterLayoutSize();

        //data = new ArrayList<>();
        /*
        data = new ArrayList<>();

        if ( MovieDatabase != null) {
            for (int i = 0; i < 10; i++) {
                try {
                    data.add(JsonUtils.parseMovieData(MovieDatabase.getJSONObject(i), this.getBaseContext(),"hello01"));
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }

        if( data != null) {
            adapter = new PosterAdapter(data,posterLayoutSize.getParams());
        }
        */

        /*  SWITCH *///////////////
        //recyclerView.setAdapter(adapter);
        mAdapter = new PosterAdapter(this, posterLayoutSize.getParams());
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, mCursorArgs, this);
        recyclerView.setAdapter(mAdapter);
    }


    /*  Check NETWORK CONNECTION && make QUERY */
    private void makeTMDBQuery(String query) {
        URL searchURL = NetworkUtils.buildURI(query);
        currentFilterKey = query;

        mCursorArgs.clear();
        mCursorArgs.putString("query",query);

        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet = cm.getActiveNetworkInfo();
        boolean activeNetwork = activeNet != null && activeNet.isConnectedOrConnecting();

        if(activeNetwork) {
            new TheMovieDatabaseQuery().execute(searchURL);
        }

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
                //mResultsTextView.setText(databaseQuery);
                try {
                    JSONObject baseJsonResult = new JSONObject(databaseQuery);
                    MovieDatabase = baseJsonResult.getJSONArray("results");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            } else {
                //mResultsTextView.setText(R.string.Error_Data);
            }


            //populate data and refresh adapter
            //Clear data
            //data.clear();


            //if ( MovieDatabase != null) {
                for (int i = 0; i < MovieDatabase.length(); i++) {
                    try {
                        JsonUtils.parseMovieData(MovieDatabase.getJSONObject(i), getBaseContext().getApplicationContext(),currentFilterKey);
                    } catch (JSONException ex) {
                        ex.printStackTrace();
                    }
                }
            //}

            //adapter = new PosterAdapter(data, posterLayoutSize.getParams());
            //recyclerView.setAdapter(adapter);
            mAdapter = new PosterAdapter(getBaseContext(), posterLayoutSize.getParams());
            //recyclerView.swapAdapter(mAdapter,true);
            //recyclerView.swapAdapter(adapter, true);

            //mAdapter = new PosterAdapter(getBaseContext(), posterLayoutSize.getParams());
            //recyclerView.swapAdapter(mAdapter, true);
        }
    }

    /* Additions for CURSOR */////////
    @Override
    protected void onResume() {
        super.onResume();
        //getSupportLoaderManager().restartLoader(TASK_LOADER_ID,mCursorArgs,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, final Bundle loaderArgs) {
        return new AsyncTaskLoader<Cursor>(this) {
            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if(mMovieData != null) {
                    deliverResults(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContentResolver().query(MovieContract.Movies.CONTENT_URI,
                            null,
                            "filter = '" + loaderArgs.getString("query") + "'",
                            null,
                            MovieContract.Movies.COLUMN_TITLE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            public void deliverResults(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        recyclerView.swapAdapter(mAdapter, true);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
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
        List<SpinnerValues> spinnerValues = new ArrayList<>();
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
        ArrayAdapter<SpinnerValues> spin_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerValues);
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
        //makeTMDBQuery(parent.getItemAtPosition(pos).toString());
        SpinnerValues sv = (SpinnerValues) parent.getItemAtPosition(pos);
        currentFilterKey = sv.getKey();
        mCursorArgs.clear();
        mCursorArgs.putString("query",currentFilterKey);
        makeTMDBQuery(sv.getKey());
        getSupportLoaderManager().restartLoader(TASK_LOADER_ID,mCursorArgs,this);
    }
    public void onNothingSelected(AdapterView<?> parent) {
        // Do Nothing
    }

    private class SpinnerValues {
        final String value;
        final String key;

        SpinnerValues(String value, String key) {
            this.value = value;
            this.key = key;
        }

        @Override
        public String toString() {
            return value;
        }

        String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }
    }
}
