package com.example.jonathan.moviedatabase_stage2.Utils;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.jonathan.moviedatabase_stage2.R;
import com.example.jonathan.moviedatabase_stage2.movieDetail;
import com.squareup.picasso.Picasso;

import static com.example.jonathan.moviedatabase_stage2.Data.MovieContract.Movies;

public class PosterAdapter extends RecyclerView.Adapter<PosterAdapter.CustomViewHolder>
{

    private final LinearLayout.LayoutParams params;
    private View.OnClickListener mClickListener;

    /* Cursor Additions *///////
    private static Cursor mCursor;
    private Context mContext;
    private static int sPosition;

    /* Constructor */
    public PosterAdapter(Context mContext, LinearLayout.LayoutParams params) {
        this.params = params;
        this.mContext = mContext;
    }

    /* ViewHolder to fill RV */
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate to view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.poster_view, parent, false);

        view.setOnClickListener(mClickListener);

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

        String posterUrl = mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_POSTER_URL));

        Picasso.with(imageViewPoster.getContext())
                .load("http://image.tmdb.org/t/p/w342//" + posterUrl)
                .into(imageViewPoster);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final ImageView moviePoster;

        private CustomViewHolder(View itemView) {
            super(itemView);
            this.moviePoster = itemView.findViewById(R.id.moviePoster_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View itemView) {
            Intent intent = new Intent(this.itemView.getContext(), movieDetail.class);

            mCursor.moveToPosition(getAdapterPosition());
            intent.putExtra("originalTitle",mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_TITLE)));
            intent.putExtra("overview",mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_OVERVIEW)));
            intent.putExtra("rating",mCursor.getDouble(mCursor.getColumnIndex(Movies.COLUMN_RATING)));
            intent.putExtra("posterPath",mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_POSTER_URL)));
            intent.putExtra("releaseDate",mCursor.getString(mCursor.getColumnIndex(Movies.COLUMN_RELEASE)));
            intent.putExtra("favorite", mCursor.getInt(mCursor.getColumnIndex(Movies.COLUMN_FAV)));
            intent.putExtra("id",mCursor.getInt(mCursor.getColumnIndex(Movies.COLUMN_ID)));

            itemView.getContext().startActivity(intent);
        }
    }

}
