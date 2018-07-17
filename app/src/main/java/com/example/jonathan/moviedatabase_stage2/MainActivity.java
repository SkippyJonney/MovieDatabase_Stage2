package com.example.jonathan.moviedatabase_stage2;

import android.annotation.SuppressLint;
import android.os.Parcelable;
import android.support.annotation.NonNull;
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
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.jonathan.moviedatabase_stage2.Data.MovieContract;
import com.example.jonathan.moviedatabase_stage2.Utils.JsonUtils;
import com.example.jonathan.moviedatabase_stage2.Utils.NetworkUtils;
import com.example.jonathan.moviedatabase_stage2.Utils.PosterAdapter;
import com.example.jonathan.moviedatabase_stage2.Utils.PosterLayoutSize;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView recyclerView;
    private GridLayoutManager gridLayoutManager;
    private static int currentVisiblePosition;
    /* Cursor Adapter */
    private PosterAdapter mAdapter;
    // const for unique loader
    //private static final String TAG = MainActivity.class.getSimpleName();
    private static final int TASK_LOADER_ID = 0;
    private final Bundle mCursorArgs = new Bundle();
    private static String currentFilterKey = "";

    private static JSONArray MovieDatabase;
    // for saved instance state
    private static String POSITION_KEY = "POSITION";
    private static String FILTER_KEY = "FILTER";


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

        recyclerView = findViewById(R.id.movie_poster_rv);
        recyclerView.setHasFixedSize(true);

        gridLayoutManager = new GridLayoutManager(this, 2);



        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //Get JSON Data
        currentFilterKey = "top_rated";
        if(savedInstanceState != null) {
            currentFilterKey = savedInstanceState.getString(FILTER_KEY);
        }
        makeTMDBQuery(currentFilterKey);

        //Get Layout Size
        PosterLayoutSize posterLayoutSize = new PosterLayoutSize();

        mAdapter = new PosterAdapter(this, posterLayoutSize.getParams());
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, mCursorArgs, this);
        recyclerView.setAdapter(mAdapter);
        gridLayoutManager.scrollToPosition(currentVisiblePosition);

        if(savedInstanceState != null) {
            //gridLayoutManager.scrollToPosition(savedInstanceState.getInt(POSITION_KEY));
            currentVisiblePosition = savedInstanceState.getInt(POSITION_KEY);
            Log.d("VIS", "restoring in onCreate");
            Log.d("VIS", Integer.toString(savedInstanceState.getInt(POSITION_KEY)));
            gridLayoutManager.scrollToPosition(currentVisiblePosition);
        }
    }


    /*  Check NETWORK CONNECTION && make QUERY */
    private void makeTMDBQuery(String query) {
        URL searchURL = NetworkUtils.buildURI(query);
        currentFilterKey = query;

        String inputString = "filter = '" + currentFilterKey + "'";

        mCursorArgs.clear();
        mCursorArgs.putString("query",inputString);

        /*
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet;
        assert cm != null;
        activeNet = cm.getActiveNetworkInfo();
        boolean activeNetwork = activeNet != null && activeNet.isConnectedOrConnecting();

        if(activeNetwork) {
            new TheMovieDatabaseQuery(this).execute(searchURL);
        }
        */

        if(NetworkUtils.checkNetworkStatus(this)) {
            new TheMovieDatabaseQuery(this).execute(searchURL);
        }

    }

    private static class TheMovieDatabaseQuery extends AsyncTask<URL,Void,String> {

        // Weak reference for garbage collection.
        private final WeakReference<MainActivity> activityReference;
        TheMovieDatabaseQuery(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

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
            }

            for (int i = 0; i < MovieDatabase.length(); i++) {
                try {
                    JsonUtils.parseMovieData(MovieDatabase.getJSONObject(i), activityReference.get().getBaseContext().getApplicationContext(),currentFilterKey);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            //mAdapter = new PosterAdapter(activityReference.get().getBaseContext().getApplicationContext(), posterLayoutSize.getParams());
        }
    }

    /* Additions for CURSOR */////////
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("AAAAAAAAAAAAAAAA", "Calling OnResume " + Integer.toString(currentVisiblePosition));
        gridLayoutManager.scrollToPosition(currentVisiblePosition);
    }




    @Override
    protected void onPause() {
        super.onPause();

        currentVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition();
        Log.d("AAAAAAAAAAAAAAAA", "Calling OnStop " + Integer.toString(currentVisiblePosition));

    }

    @Override
    protected void onStop() {
        super.onStop();

        currentVisiblePosition = gridLayoutManager.findFirstVisibleItemPosition();
        Log.d("AAAAAAAAAAAAAAAA", "Calling OnStop " + Integer.toString(currentVisiblePosition));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        //get first visible item
        int position = gridLayoutManager.findFirstVisibleItemPosition();
        savedInstanceState.putInt(POSITION_KEY, position);
        savedInstanceState.putString(FILTER_KEY, currentFilterKey);

        Log.d("AAAAAAAAAAAAAAAA", "Calling SaveInstanceState " + Integer.toString(position));

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d("AAAAAAAAAAAAAAAA", "Calling RestoreInstanceState " + Integer.toString(savedInstanceState.getInt(POSITION_KEY)));
        currentFilterKey = savedInstanceState.getString(FILTER_KEY);
        currentVisiblePosition = savedInstanceState.getInt(POSITION_KEY);


    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
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
                            loaderArgs.getString("query"),
                            null,
                            MovieContract.Movies.COLUMN_TITLE);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    return null;
                }
            }

            private void deliverResults(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
        recyclerView.swapAdapter(mAdapter, true);

        recyclerView.scrollToPosition(currentVisiblePosition);

        /*
        if(currentVisiblePosition != 0) {
            gridLayoutManager.scrollToPosition(currentVisiblePosition);
            currentVisiblePosition = 0;
        }
        */
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
        if(currentVisiblePosition != 0) {
            gridLayoutManager.scrollToPosition(currentVisiblePosition);
            currentVisiblePosition = 0;
        }
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

        ArrayAdapter<SpinnerValues> spin_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerValues);
        spin_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(spin_adapter);
        spinner.setOnItemSelectedListener(this);
        return true;
    }

    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        SpinnerValues sv = (SpinnerValues) parent.getItemAtPosition(pos);
        currentFilterKey = sv.getKey();
        mCursorArgs.clear();

        if(currentFilterKey.equals("favorite")) {
            mCursorArgs.putString("query", "favorite = 1");
        } else {
            String query = "filter = '" + currentFilterKey + "'";
            mCursorArgs.putString("query", query);
            makeTMDBQuery(sv.getKey());
        }

        getSupportLoaderManager().restartLoader(TASK_LOADER_ID, mCursorArgs,this);
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

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 600;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if(nColumns < 2) return 2;
        return nColumns;
    }
}
