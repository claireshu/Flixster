package com.claireshu.flickster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by claireshu on 6/15/16.
 */
public class Movie {

    public String getTitle() {
        return title;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterUrl() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", posterUrl);
    }

    public String getBackdropUrl() {
        return String.format("https://image.tmdb.org/t/p/w342/%s", backdropUrl);
    }

    public int getPopularity() {
        return popularity;
    }

    public int getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

    public String title;
    public String posterUrl;
    public String overview;
    public String backdropUrl;
    public int rating;
    public int id;

    public int popularity;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.overview = jsonObject.getString("overview");
        this.title = jsonObject.getString("title");
        this.posterUrl = jsonObject.getString("poster_path");
        this.backdropUrl = jsonObject.getString("backdrop_path");
        this.rating = jsonObject.getInt("vote_average");
        this.popularity = jsonObject.getInt("popularity");
        this.id = jsonObject.getInt("id");
    }

    public static ArrayList<Movie> fromJsonArray(JSONArray array){
        ArrayList<Movie> results = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            try {
                results.add(new Movie(array.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return results;
    }

}
