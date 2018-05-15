package com.example.jonathan.moviedatabase_stage1;

import android.content.Context;
import android.os.Debug;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.jonathan.moviedatabase_stage1.Data.MovieDataModel;
import com.example.jonathan.moviedatabase_stage1.Data.sampleMovieData;
import com.example.jonathan.moviedatabase_stage1.Utils.PosterAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private GridLayoutManager gridManager;
    private static RecyclerView recyclerView;
    private static ArrayList<MovieDataModel> data;
    public static View.OnClickListener posterOnClickListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        posterOnClickListener = new PosterOnClickListener(this);

        recyclerView = findViewById(R.id.movie_poster_rv);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(this);
        gridManager = new GridLayoutManager(this,2);

        recyclerView.setLayoutManager(gridManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        data = new ArrayList<MovieDataModel>();
        for(int i = 0; i < sampleMovieData.Length(); i++) {
            data.add(new MovieDataModel(sampleMovieData.getString(i)));
        }

        adapter = new PosterAdapter(data);
        recyclerView.setAdapter(adapter);
    }


    private static class PosterOnClickListener implements View.OnClickListener {

        private final Context context;

        private PosterOnClickListener(Context context) {
            this.context = context;
        }

        @Override
        public void onClick(View v) {
            LaunchDetails(v);
        }

        private void LaunchDetails(View v) {
            Log.d("VIEW:", v.toString());
        }
    }


}
