package com.example.jonathan.moviedatabase_stage1.Utils;

import java.util.ArrayList;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jonathan.moviedatabase_stage1.Data.MovieDataModel;
import com.example.jonathan.moviedatabase_stage1.MainActivity;
import com.example.jonathan.moviedatabase_stage1.R;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.CustomViewHolder>
{

    private ArrayList<MovieDataModel> dataSet;

    //Custom View Holder
    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView movieName;
        ImageView moviePoster;

        public CustomViewHolder(View itemView) {
            super(itemView);
            this.movieName = (TextView) itemView.findViewById(R.id.movieTitle_tv);
            this.moviePoster = (ImageView) itemView.findViewById(R.id.moviePoster_iv);
        }
    }

    public PosterAdapter(ArrayList<MovieDataModel> data) {
        this.dataSet = data;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poster_view,parent,false);

        view.setOnClickListener(MainActivity.posterOnClickListener);

        CustomViewHolder myViewHolder = new CustomViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int listPosition) {
        TextView textViewTitle = holder.movieName;
        ImageView imageViewPoster = holder.moviePoster;

        textViewTitle.setText(dataSet.get(listPosition).getTitle());
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
