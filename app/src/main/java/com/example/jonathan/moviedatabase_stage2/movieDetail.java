package com.example.jonathan.moviedatabase_stage2;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jonathan.moviedatabase_stage2.Data.MovieContract;
import com.example.jonathan.moviedatabase_stage2.Utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class movieDetail extends AppCompatActivity implements NetworkUtils.onReviewReady {

    public Button favoriteBtn;
    public Button playTrailerBtn;

    String originalTitle = null;
    String overview = null;
    Double rating = null;
    String posterPath = null;
    String releaseDate = null;
    Integer favorite =  0;
    Integer id = 0;

    public TextView mReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        //Setup Favorite Button
        favoriteBtn = (Button) findViewById(R.id.favorite_btn);
        playTrailerBtn = (Button) findViewById(R.id.playTrailerBtn);

        playTrailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetworkUtils.queryTMDB_trailer(id,getApplicationContext());
            }
        });

        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // do something
                ContentValues mUpdatedValues = new ContentValues();
                String mSelectionClause = MovieContract.Movies.COLUMN_TITLE + "= '" + originalTitle + "'";
                String[] mSelectionArgs = {originalTitle};

                int mRowsUpdated = 0;

                // toggle favorite
                String toast_result;
                if( favorite == 0 ) {
                    mUpdatedValues.put("favorite", true);
                    toast_result = " ADDED to favorites";
                    favorite = 1;
                    setButtonTint(favoriteBtn, R.color.buttonColorSelected);
                } else {
                    mUpdatedValues.put("favorite", false);
                    toast_result = " REMOVED from favorites";
                    favorite = 0;
                    setButtonTint(favoriteBtn, R.color.buttonColorDefault);
                }

                //mUpdatedValues.put("favorite", true);

                mRowsUpdated = getContentResolver().update(
                        MovieContract.Movies.CONTENT_URI,
                        mUpdatedValues,
                        mSelectionClause,
                        mSelectionArgs
                    );

                //Log.d("BTN:", "Button Reached");
                //Log.d("BTN:", Integer.toString(mRowsUpdated));

                // Make Toast to notify user of background change.
                Toast toast = Toast.makeText(
                        getApplicationContext(),
                        originalTitle + toast_result,
                        Toast.LENGTH_SHORT);
                toast.show();

            }
        });

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
        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);



        try {
            // Get Data
            Intent intent = getIntent();
            originalTitle = Objects.requireNonNull(intent.getExtras()).getString("originalTitle");
            overview = intent.getExtras().getString("overview");
            rating = intent.getExtras().getDouble("rating");
            posterPath = intent.getExtras().getString("posterPath");
            releaseDate = intent.getExtras().getString("releaseDate");
            favorite = intent.getExtras().getInt("favorite");
            id = intent.getExtras().getInt("id");
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

        // Init Views
        TextView mOriginalTitle = findViewById(R.id.originalTitle_tv);
        ImageView mImageView = findViewById(R.id.poster_iv);
        TextView mRating = findViewById(R.id.rating_tv);
        TextView mReleaseDate = findViewById(R.id.releaseDate_tv);
        TextView mOverview = findViewById(R.id.overview_tv);
        mReview = findViewById(R.id.review_tv);



        // Assign Data
        mOriginalTitle.setText(originalTitle);
        mRating.setText(R.string.Average_Rating);
        mRating.append(String.format(Locale.US,"%.2f",rating));
        mOverview.setText(overview);
        mReleaseDate.setText(dateFormatter(releaseDate));
        Picasso.with(mImageView.getContext())
                .load("http://image.tmdb.org/t/p/w342//" + posterPath)
                .into(mImageView);
        mReview.setText(" ... loading ...");

        // Update Favorite Button Color
        if(favorite == 1) { setButtonTint(favoriteBtn, R.color.buttonColorSelected); }

        // Get review
        NetworkUtils.queryTMDB_Reviews(id, this, this::onTaskCompleted);

    }

    //Support Back Button
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    //Date Formatter
    private String dateFormatter(String str) {

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

    private void setButtonTint(Button btn, Integer colors) {
        btn.setBackgroundTintList(getColorStateList(colors));
    }



    public void onTaskCompleted(String review) {
        // set review text
        if(review != null) {
            mReview.setText(review);
        }
    }
}
