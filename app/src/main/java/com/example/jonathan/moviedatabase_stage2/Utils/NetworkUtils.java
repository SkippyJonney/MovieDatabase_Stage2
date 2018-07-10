package com.example.jonathan.moviedatabase_stage2.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.jonathan.moviedatabase_stage2.BuildConfig;
import com.example.jonathan.moviedatabase_stage2.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private final static String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie";
    private final static String TMDB_VIDEO_PATH = "videos";
    private final static String TMDB_REVIEW_PATH = "reviews";

    private final static String YOUTUBE_BASE_URL = "https://www.youtube.com/";
    private final static String YOUTUBE_PATH_WATCH = "watch";
    private final static String YOUTUBE_PARAM_V = "v";


    private final static String PARAM_API = "api_key";
    private final static String API_KEY = BuildConfig.ApiKey;


    // Get Url using PARAM. Implements API key
    public static URL buildURI(String param) {
        Uri newUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(param)
                .appendQueryParameter(PARAM_API,API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(newUri.toString());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        return url;
    }

    // Get Url to TrailerKey
    private static URL buildTMDB_Query(String movieID, String path) {
        Uri newUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                .appendPath(movieID)
                .appendPath(path)
                .appendQueryParameter(PARAM_API, API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(newUri.toString());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        return url;
    }

    // Get Youtube Video URL
    private static URL buildYoutubeURI(String param) {
        Uri newUri = Uri.parse(YOUTUBE_BASE_URL).buildUpon()
                .appendPath(YOUTUBE_PATH_WATCH)
                .appendQueryParameter(YOUTUBE_PARAM_V, param)
                .build();
        URL url = null;

        try { url = new URL(newUri.toString()); }
        catch (MalformedURLException ex) { ex.printStackTrace(); }

        return url;
    }



    // Get Result from HTTP server
    public static String getResponseHTTP(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("//A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    // Check Network Status
    public static Boolean checkNetworkStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNet;
        assert cm != null;
        activeNet = cm.getActiveNetworkInfo();
        return activeNet != null && activeNet.isConnectedOrConnecting();
    }

    public static void queryTMDB_trailer(Integer movieID, Context context) {

        // Make URL from movie Id
        URL searchURL = buildTMDB_Query(Integer.toString(movieID), TMDB_VIDEO_PATH);
        // If Network Active -- query Server
        if(checkNetworkStatus(context)) {
            new TMDB_TRAILER_QUERY(context).execute(searchURL);
        }

    }

    public static void queryTMDB_Reviews(Integer movieID, Context context, onReviewReady delegate) {
        URL searchURL = buildTMDB_Query(Integer.toString(movieID), TMDB_REVIEW_PATH);
        if(checkNetworkStatus(context)) {
            new TMDB_REVIEW_QUERY(context, delegate).execute(searchURL);
        }
    }


    public static class TMDB_REVIEW_QUERY extends AsyncTask<URL, Void, String> {
        static JSONArray jsonArrReview;

        public onReviewReady delegate = null;
        String review = "No Reviews";

        // Setup Context
        private WeakReference<Context> contextRef;
        private TMDB_REVIEW_QUERY(Context context, onReviewReady delegate) {
            contextRef = new WeakReference<>(context.getApplicationContext());
            this.delegate = delegate;
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String databaseQuery = null;

            try {
                databaseQuery = NetworkUtils.getResponseHTTP(searchUrl);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return databaseQuery;
        }

        @Override
        protected void onPostExecute(String result) {

            if(result != null) {
                try {
                    JSONObject baseJsonResult = new JSONObject(result);
                    jsonArrReview = baseJsonResult.getJSONArray("results");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }



            try {
                review = JsonUtils.getReviewText(jsonArrReview.getJSONObject(0), contextRef.get().getApplicationContext());
            } catch (JSONException ex) {
                ex. printStackTrace();
            }


            Log.d("-----------------------", review);
            // Post Results to UI
            delegate.onTaskCompleted(review);
        }

    }


    public static class TMDB_TRAILER_QUERY extends AsyncTask<URL,Void,String> {

        static JSONArray jsonArrTrailer;
        static String movieTrailerKey = "none";

        // Context Weak Reference
        private WeakReference<Context> contextRef;
        //private Context contextRef;
        private TMDB_TRAILER_QUERY(Context context) {
            contextRef = new WeakReference<>(context.getApplicationContext());
            //contextRef = context;
        }

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String databaseQuery = null;

            //Log.d("HELP ME:", searchUrl.toString());

            try {
                databaseQuery = NetworkUtils.getResponseHTTP(searchUrl);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return databaseQuery;
        }

        @Override
        protected void onPostExecute(String result) {

            if(result != null) {
                try {
                    JSONObject baseJsonResult = new JSONObject(result);
                    jsonArrTrailer = baseJsonResult.getJSONArray("results");
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }

            // extract video id
            try {
                movieTrailerKey = JsonUtils.getTrailerKey(jsonArrTrailer.getJSONObject(0), contextRef.get().getApplicationContext());
            } catch (JSONException ex) {
                ex.printStackTrace();
            }

            if(movieTrailerKey != null) {
                openYouTube(buildYoutubeURI(movieTrailerKey).toString());
            }


        }

        // Webpage Intent
        private void openYouTube(String url) {
            Uri webpage = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            if (intent.resolveActivity(contextRef.get().getPackageManager()) != null) {
                contextRef.get().startActivity(intent);
            }
        }


    }

    public interface onReviewReady {
        void onTaskCompleted(String review);
    }




}
