package com.example.jonathan.moviedatabase_stage1.Utils;
import com.example.jonathan.moviedatabase_stage1.Data.MovieDataModel;

import org.json.JSONObject;

public class JsonUtils {

    public static MovieDataModel parseMovieData(JSONObject json) {
        //Create Empty Movie Model
        MovieDataModel newMovie = new MovieDataModel();

        //Parse Json into Movie Model
        newMovie.setOriginalTitle(json.optString("original_title"));
        newMovie.setOverview(json.optString("overview"));
        newMovie.setPosterPath(json.optString("poster_path"));
        newMovie.setRating(json.optString("vote_average"));
        newMovie.setReleaseDate(json.optString("release_date"));


        return newMovie;
    }


}
