package com.example.jonathan.moviedatabase_stage1.Data;

public class MovieDataModel {

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
}
