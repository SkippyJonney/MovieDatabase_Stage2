package com.example.jonathan.moviedatabase_stage2.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.jonathan.moviedatabase_stage2.Data.MovieContract.Movies;

public class MovieDBHelper extends SQLiteOpenHelper {

    // Database Name
    private static final String DB_NAME = "movieDB.db";

    // schema version
    private static final int VERSION = 2;


    MovieDBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Create movie table
        final String CREATE_TABLE = "CREATE TABLE " + Movies.TABLE_NAME + " (" +
                Movies._ID                  + " INTEGER PRIMARY KEY, " +
                Movies.COLUMN_FILTER        + " TEXT NOT NULL, " +
                Movies.COLUMN_TITLE         + " TEXT NOT NULL UNIQUE, " +
                Movies.COLUMN_OVERVIEW      + " TEXT NOT NULL, " +
                Movies.COLUMN_RATING        + " DOUBLE NOT NULL, " +
                Movies.COLUMN_RELEASE       + " TEXT NOT NULL, " +
                Movies.COLUMN_POSTER_URL    + " TEXT NOT NULL);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Movies.TABLE_NAME);
        onCreate(db);
    }
}
