package com.example.jonathan.moviedatabase_stage1.Utils;

import android.net.Uri;

import com.example.jonathan.moviedatabase_stage1.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

public class NetworkUtils {

    private final static String TMDB_BASE_URL = "https://api.themoviedb.org/3/movie";

    final static String PATH_POPULAR = "popular";
    final static String PATH_RATED = "top_rated";

    /* TODO 1 Remove API Key from github push */
    private final static String PARAM_API = "api_key";
    //final static String API_KEY = "fa6ed759f59df5cc1a528d291ae8165d";
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


}
