package com.example.jonathan.moviedatabase_stage2.Utils;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

import com.example.jonathan.moviedatabase_stage2.Data.MovieContract;

import org.json.JSONObject;

public class JsonUtils {

    public static void parseMovieData(JSONObject json, Context context, String filterKey) {

        //Add movie to SQL Database
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.Movies.COLUMN_FILTER, filterKey);
        contentValues.put(MovieContract.Movies.COLUMN_ID, json.optString("id"));
        contentValues.put(MovieContract.Movies.COLUMN_TITLE, json.optString("original_title"));
        contentValues.put(MovieContract.Movies.COLUMN_OVERVIEW, json.optString("overview"));
        contentValues.put(MovieContract.Movies.COLUMN_POSTER_URL, json.optString("poster_path"));
        contentValues.put(MovieContract.Movies.COLUMN_RATING, json.optString("vote_average"));
        contentValues.put(MovieContract.Movies.COLUMN_RELEASE, json.optString("release_date"));

        // insert via content resolver
        Uri uri = context.getContentResolver().insert(MovieContract.Movies.CONTENT_URI, contentValues);

    }


    public static String getTrailerKey(JSONObject json, Context context) {

        return json.optString("key");
    }

    public static String getReviewText(JSONObject json, Context context) {
        return json.optString("content");
    }


}
