package com.example.jonathan.moviedatabase_stage1.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDataModel implements Parcelable {

    private String originalTitle;
    private String overview;
    private Double rating;
    private String posterPath;
    private String releaseDate;

    /* No Argument Constructer // Serialization */
    public MovieDataModel() {
        originalTitle = "no data";
    }

    public MovieDataModel(String originalTitle, String overview, Double rating, String posterPath, String releaseDate) {
        this.originalTitle = originalTitle;
        this.overview = overview;
        this.rating = rating;
        this.posterPath = posterPath;
        this.releaseDate = releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }
    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }
    public String getOverview() {
        return overview;
    }
    public void setOverview(String overview) {
        this.overview = overview;
    }
    public Double getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = Double.parseDouble(rating);
    }
    public String getPosterPath() {
        return posterPath;
    }
    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }
    public String getReleaseDate() {
        return releaseDate;
    }
    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    /* Make this class parsable */


    // INTERFACE //
    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(originalTitle);
        out.writeString(overview);
        out.writeDouble(rating);
        out.writeString(posterPath);
        out.writeString(releaseDate);
    }

    private MovieDataModel(Parcel in) {
        originalTitle = in.readString();
        overview = in.readString();
        rating = in.readDouble();
        posterPath = in.readString();
        releaseDate = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable CREATOR //
    public static final Parcelable.Creator<MovieDataModel> CREATOR
            = new Parcelable.Creator<MovieDataModel>() {
        @Override
        public MovieDataModel createFromParcel(Parcel in) {
            return new MovieDataModel(in);
        }
        @Override
        public MovieDataModel[] newArray(int size) {
            return new MovieDataModel[size];
        }
    };
}
