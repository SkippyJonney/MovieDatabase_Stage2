package com.example.jonathan.moviedatabase_stage2.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import static com.example.jonathan.moviedatabase_stage2.Data.MovieContract.Movies.TABLE_NAME;

public class MovieContentProvider extends ContentProvider {


    // Setup URI Matcher
    public static final int MOVIES = 100;
    public static final int MOVIE_WITH_ID = 101;
    public static final int MOVIES_FAV = 200;
    public static final UriMatcher sUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_MOVIES + "/#", MOVIE_WITH_ID);
        uriMatcher.addURI(MovieContract.AUTHORITY, MovieContract.PATH_FAV, MOVIES_FAV);

        return uriMatcher;
    }

    private MovieDBHelper mMovieDBHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mMovieDBHelper = new MovieDBHelper(context);

        return true;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mMovieDBHelper.getReadableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor retCursor;

        switch(match) {
            case MOVIES:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case MOVIES_FAV:
                retCursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        // use throughout function
        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch(match) {
            case MOVIES:
                // insert values into Movie table
                try {
                    long id = db.insertOrThrow(TABLE_NAME, null, values);

                    returnUri = ContentUris.withAppendedId(MovieContract.BASE_CONTENT_URI, id);

                } catch (SQLiteConstraintException ex) {
                    // do nothing for now
                    // ignore duplicate
                    returnUri = Uri.EMPTY;
                }

                /*if(id > -1) {
                    returnUri = ContentUris.withAppendedId(MovieContract.BASE_CONTENT_URI, id);

                }
                else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                */
                break;

                // default throws UnsupportedOppExcept
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        Log.d("SQL:Q", returnUri.toString());
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        // use throughout function
        final SQLiteDatabase db = mMovieDBHelper.getWritableDatabase();

        int match = sUriMatcher.match(uri);
        int updatedRows = 0;

        switch(match) {
            case MOVIES:
                // insert values into Movie table
                updatedRows = db.update(TABLE_NAME, values, selection, null);
                break;
            // default throws UnsupportedOppExcept
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return updatedRows;
    }
}
