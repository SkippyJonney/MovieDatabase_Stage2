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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class movieDetail extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //Setup AppBar
        Toolbar toolbar = findViewById(R.id.movieDetail_menu);
        setSupportActionBar(toolbar);

        ActionBar ab = getSupportActionBar();
        try {
            Objects.requireNonNull(ab).setDisplayShowHomeEnabled(true);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
        Objects.requireNonNull(ab).setDisplayHomeAsUpEnabled(true);
        ab.setTitle(R.string.app_name);

        //Dim UI
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);


        String originalTitle = null;
        String overview = null;
        Double rating = null;
        String posterPath = null;
        String releaseDate = null;
        try {
            // Get Data
            Intent intent = getIntent();
            originalTitle = Objects.requireNonNull(intent.getExtras()).getString("originalTitle");
            overview = intent.getExtras().getString("overview");
            rating = intent.getExtras().getDouble("rating");
            posterPath = intent.getExtras().getString("posterPath");
            releaseDate = intent.getExtras().getString("releaseDate");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        // Init Views
        TextView mOriginalTitle = findViewById(R.id.originalTitle_tv);
        ImageView mImageView = findViewById(R.id.poster_iv);
        TextView mRating = findViewById(R.id.rating_tv);
        TextView mReleaseDate = findViewById(R.id.releaseDate_tv);
        TextView mOverview = findViewById(R.id.overview_tv);


        // Assign Data
        mOriginalTitle.setText(originalTitle);
        mRating.setText(R.string.Average_Rating);
        mRating.append(String.format(Locale.US,"%.2f",rating));
        mOverview.setText(overview);
        mReleaseDate.setText(dateFormater(releaseDate));
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

    //Date Formater
    private String dateFormater(String str) {

        String inPattern  = "yyyy-mm-dd";
        String outPattern = "MMM d, yyyy";
        String date = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat(inPattern, Locale.US);

        try {
            Date newD = dateFormat.parse(str);
            dateFormat.applyPattern(outPattern);
            date = dateFormat.format(newD);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        if(date != null) {
            return date;
        } else {
            return str;
        }
    }
}
