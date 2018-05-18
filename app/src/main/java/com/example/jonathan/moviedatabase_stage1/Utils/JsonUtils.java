package com.example.jonathan.moviedatabase_stage1.Utils;
import com.example.jonathan.moviedatabase_stage1.Data.MovieDataModel;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonUtils {

    public static MovieDataModel parseMovieData(JSONObject json) {
        //Create Empty Movie Model
        MovieDataModel newMovie = new MovieDataModel();

        JSONObject rootObject = json;

        //Parse Json into Movie Model
        newMovie.setOriginalTitle(rootObject.optString("original_title"));
        newMovie.setOverview(rootObject.optString("overview"));
        newMovie.setPosterPath(rootObject.optString("poster_path"));
        newMovie.setRating(rootObject.optString("vote_average"));
        newMovie.setReleaseDate(rootObject.optString("release_date"));


        return newMovie;
    }


}
