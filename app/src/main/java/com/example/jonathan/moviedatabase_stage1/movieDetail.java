package com.example.jonathan.moviedatabase_stage1;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class movieDetail extends AppCompatActivity {

    // View Variables
    private TextView mOriginalTitle;
    private ImageView mImageView;
    private TextView mRating;
    private TextView mReleaseDate;
    private TextView mOverview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //Setup AppBar
        Toolbar toolbar = findViewById(R.id.movieDetail_menu);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayShowHomeEnabled(true);
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.app_name);

        //Dim UI
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        // Get Data
        Intent intent = getIntent();
        final String originalTitle = intent.getExtras().getString("originalTitle");
        final String overview = intent.getExtras().getString("overview");
        final Double rating = intent.getExtras().getDouble("rating");
        final String posterPath = intent.getExtras().getString("posterPath");
        final String releaseDate = intent.getExtras().getString("releaseDate");

        // Init Views
        mOriginalTitle = findViewById(R.id.originalTitle_tv);
        mImageView = findViewById(R.id.poster_iv);
        mRating = findViewById(R.id.rating_tv);
        mReleaseDate = findViewById(R.id.releaseDate_tv);
        mOverview = findViewById(R.id.overview_tv);


        // Assign Data
        mOriginalTitle.setText(originalTitle);
        mRating.setText(String.format("%.2f",rating));
        mOverview.setText(overview);
        mReleaseDate.setText(releaseDate);
        Picasso.with(mImageView.getContext())
                .load("http://image.tmdb.org/t/p/w342//" + posterPath)
                .into(mImageView);

    }

    //Support Back Button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
