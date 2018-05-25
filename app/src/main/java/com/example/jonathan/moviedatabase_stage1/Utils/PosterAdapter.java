package com.example.jonathan.moviedatabase_stage1.Utils;

import java.util.ArrayList;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jonathan.moviedatabase_stage1.Data.MovieDataModel;
import com.example.jonathan.moviedatabase_stage1.MainActivity;
import com.example.jonathan.moviedatabase_stage1.R;
import com.example.jonathan.moviedatabase_stage1.movieDetail;
import com.squareup.picasso.Picasso;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.CustomViewHolder>
{

    private static ArrayList<MovieDataModel> dataSet;
    private final LinearLayout.LayoutParams params;

    private OnItemClicked onClick;
    public interface OnItemClicked {
        void onItemClick();
    }

    //Custom View Holder
    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView movieName;
        final ImageView moviePoster;
        private AdapterView.OnItemClickListener itemClickListener;

        CustomViewHolder(View itemView) {
            super(itemView);
            this.moviePoster = itemView.findViewById(R.id.moviePoster_iv);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            Log.d("VIEW:", "Hello from the string");
            Intent intent = new Intent(this.itemView.getContext(), movieDetail.class);

            intent.putExtra("originalTitle",dataSet.get(getAdapterPosition()).getOriginalTitle());
            intent.putExtra("overview",dataSet.get(getAdapterPosition()).getOverview());
            intent.putExtra("rating",dataSet.get(getAdapterPosition()).getRating());
            intent.putExtra("posterPath",dataSet.get(getAdapterPosition()).getPosterPath());
            intent.putExtra("releaseDate",dataSet.get(getAdapterPosition()).getReleaseDate());

            itemView.getContext().startActivity(intent);
        }

        void setItemClickListener(AdapterView.OnItemClickListener clickListener) {
            this.itemClickListener = clickListener;
        }
    }

    public PosterAdapter(ArrayList<MovieDataModel> data, LinearLayout.LayoutParams params) {
        dataSet = data;
        this.params = params;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poster_view,parent,false);

        view.setOnClickListener(MainActivity.posterOnClickListener);

        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int listPosition) {
        //TextView textViewTitle = holder.movieName;
        ImageView imageViewPoster = holder.moviePoster;

        imageViewPoster.setLayoutParams(params);

        //textViewTitle.setText(dataSet.get(listPosition).getOriginalTitle());
        //Picasso.with(this.ImageView).load().into(ImageView);
        Picasso.with(imageViewPoster.getContext())
                .load("http://image.tmdb.org/t/p/w342//" + dataSet.get(listPosition).getPosterPath())
                .into(imageViewPoster);

        //handle click
        holder.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick.onItemClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }
}
