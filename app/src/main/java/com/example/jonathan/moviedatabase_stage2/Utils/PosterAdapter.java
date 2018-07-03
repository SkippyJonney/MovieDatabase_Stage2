package com.example.jonathan.moviedatabase_stage2.Utils;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jonathan.moviedatabase_stage2.Data.MovieDataModel;
import com.example.jonathan.moviedatabase_stage2.MainActivity;
import com.example.jonathan.moviedatabase_stage2.R;
import com.example.jonathan.moviedatabase_stage2.movieDetail;
import com.squareup.picasso.Picasso;

import static com.example.jonathan.moviedatabase_stage2.Data.MovieContract.Movies;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.CustomViewHolder>
{

    private static ArrayList<MovieDataModel> dataSet;
    private final LinearLayout.LayoutParams params;

    /* Cursor Additions *///////
    private static Cursor mCursor;
    private Context mContext;
    private static int sPosition;

    /* Constructor */
    public PosterAdapter(Context mContext, LinearLayout.LayoutParams params) {
        this.params = params;
        this.mContext = mContext;
    }

    public PosterAdapter(ArrayList<MovieDataModel> data, LinearLayout.LayoutParams params) {
        dataSet = data;
        this.params = params;
    }

    /* ViewHolder to fill RV */
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate to view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.poster_view, parent, false);

        view.setOnClickListener(MainActivity.posterOnClickListener);

        return new CustomViewHolder(view);
    }

    /* Return Item Count */
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    /* Update Cursor to newly updated cursor */
    public Cursor swapCursor(Cursor c) {
        // has cursor changed
        if(mCursor == c) {
            return null;
        }
        Cursor tmp = mCursor;
        this.mCursor = c;

        // is cursor valid?
        if(c != null) {
            this.notifyDataSetChanged();
        }
        return tmp;
    }

    /* onBindViewHolder called by Recycler View to display data at cursor pos. */
    @Override
    public void onBindViewHolder(CustomViewHolder holder, int position) {

        ImageView imageViewPoster = holder.moviePoster;
        imageViewPoster.setLayoutParams(params);

        mCursor.moveToPosition(position);
        sPosition = position;



        String posterUrl = mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_POSTER_URL));
        //String title = mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_TITLE));
        //String overview = mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_OVERVIEW));
        //String rating = mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_RATING));
        //String release = mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_RELEASE));


        Picasso.with(imageViewPoster.getContext())
                .load("http://image.tmdb.org/t/p/w342//" + posterUrl)
                .into(imageViewPoster);

        //handle click
        holder.setItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onClick.onItemClick();
            }
        });
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView moviePoster;
        private AdapterView.OnItemClickListener itemClickListener;

        // Data Fields
        String dPosterUrl;
        String dTitle;
        String dOverview;
        String dRating;
        String dRelease;

        CustomViewHolder(View itemView) {
            super(itemView);
            this.moviePoster = itemView.findViewById(R.id.moviePoster_iv);

            itemView.setOnClickListener(this);
        }

        public void bind(String dTitle, String dPosterUrl, String dOverview, String dRating, String dRelease) {
            this.dPosterUrl = dPosterUrl;
            this.dTitle = dTitle;
            this.dOverview = dOverview;
            this.dRating = dRating;
            this.dRelease = dRelease;
        }

        @Override
        public void onClick(View itemView) {
            Log.d("VIEW:", "Hello from the string");
            Intent intent = new Intent(this.itemView.getContext(), movieDetail.class);

            mCursor.moveToPosition(sPosition);
            intent.putExtra("originalTitle",mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_TITLE)));
            intent.putExtra("overview",mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_OVERVIEW)));
            intent.putExtra("rating",mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_RATING)));
            intent.putExtra("posterPath",mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_POSTER_URL)));
            intent.putExtra("releaseDate",mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_RELEASE)));

            itemView.getContext().startActivity(intent);
        }

        void setItemClickListener(AdapterView.OnItemClickListener clickListener) {
            this.itemClickListener = clickListener;
        }
    }




    private OnItemClicked onClick;
    public interface OnItemClicked {
        void onItemClick();
    }

    /*
    //Custom View Holder
    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
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
    */

    /*
    public PosterAdapter(ArrayList<MovieDataModel> data, LinearLayout.LayoutParams params) {
        dataSet = data;
        this.params = params;
    }
    */

    /*
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.poster_view,parent,false);

        view.setOnClickListener(MainActivity.posterOnClickListener);

        return new CustomViewHolder(view);
    }
    */

    /*
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
    */

    /*
    @Override
    public int getItemCount() {
        return dataSet.size();
    }
    */

    public void setOnClick(OnItemClicked onClick) {
        this.onClick = onClick;
    }
}
