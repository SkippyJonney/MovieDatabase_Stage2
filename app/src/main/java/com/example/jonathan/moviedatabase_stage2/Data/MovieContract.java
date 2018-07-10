package com.example.jonathan.moviedatabase_stage2.Data;

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {


    // Constants
    public static final String AUTHORITY = "com.example.jonathan.moviedatabase_stage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_MOVIES = "movies";
    public static final String PATH_FAV = "favorites";

    // Primary Movie Table
    public static final class Movies implements BaseColumns {

        // URI
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        // Table Name
        public static final String TABLE_NAME = "movies";
        // Columns
        public static final String COLUMN_FILTER = "filter";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_POSTER_URL = "posterUrl";
        public static final String COLUMN_RELEASE = "releaseDate";
        public static final String COLUMN_FAV = "favorite";
    }

}
