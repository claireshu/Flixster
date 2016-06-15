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

    public String title;
    public String posterUrl;
    public String overview;
    public String backdropUrl;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.overview = jsonObject.getString("overview");
        this.title = jsonObject.getString("title");
        this.posterUrl = jsonObject.getString("poster_path");
        this.backdropUrl = jsonObject.getString("backdrop_path");
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

//    @Override
//    public String toString() {
//        return title;
//    }
}
