package com.example.jonathan.moviedatabase_stage2.Utils;
import android.content.ContentValues;
import android.content.ContentProvider;
import android.content.Context;
import android.graphics.Movie;
import android.net.Uri;

import com.example.jonathan.moviedatabase_stage2.Data.MovieContentProvider;
import com.example.jonathan.moviedatabase_stage2.Data.MovieContract;
import com.example.jonathan.moviedatabase_stage2.Data.MovieDataModel;

import org.json.JSONObject;

public class JsonUtils {

    public static MovieDataModel parseMovieData(JSONObject json, Context context, String filterKey) {
        //Create Empty Movie Model
        MovieDataModel newMovie = new MovieDataModel();

        //Parse Json into Movie Model
        newMovie.setOriginalTitle(json.optString("original_title"));
        newMovie.setOverview(json.optString("overview"));
        newMovie.setPosterPath(json.optString("poster_path"));
        newMovie.setRating(json.optString("vote_average"));
        newMovie.setReleaseDate(json.optString("release_date"));

        //Add movie to SQL Database
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.Movies.COLUMN_FILTER, filterKey);
        contentValues.put(MovieContract.Movies.COLUMN_TITLE, json.optString("original_title"));
        contentValues.put(MovieContract.Movies.COLUMN_OVERVIEW, json.optString("overview"));
        contentValues.put(MovieContract.Movies.COLUMN_POSTER_URL, json.optString("poster_path"));
        contentValues.put(MovieContract.Movies.COLUMN_RATING, json.optString("vote_average"));
        contentValues.put(MovieContract.Movies.COLUMN_RELEASE, json.optString("release_date"));
            // insert via content resolver

        Uri uri = context.getContentResolver().insert(MovieContract.Movies.CONTENT_URI, contentValues);

        return newMovie;
    }


}
