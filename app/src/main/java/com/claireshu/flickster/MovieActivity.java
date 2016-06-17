package com.claireshu.flickster;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.claireshu.flickster.models.Movie;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieActivity extends AppCompatActivity {
    ArrayList<Movie> movies;
    ListView lvMovies;
    MoviesAdapter adapter;
    private SwipeRefreshLayout swipeContainer;
    AsyncHttpClient client;
    String url;
    boolean toggleMode = true;
    Typeface customFont;
    TextView tvMode;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client2;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // perform query here
                int position = -1;

                for (int i = 0; i < movies.size(); i++) {
                    if (query.equalsIgnoreCase(movies.get(i).getTitle())) {
                        position = i;
                    }
                }

                if (position != -1) {
                    Intent viewDetails = new Intent(MovieActivity.this, MovieDetails.class);

                    viewDetails.putExtra("position", position);
                    viewDetails.putExtra("title", movies.get(position).getTitle());
                    viewDetails.putExtra("rating", movies.get(position).getRating());
                    viewDetails.putExtra("popularity", movies.get(position).getPopularity());
                    viewDetails.putExtra("overview", movies.get(position).getOverview());

                    startActivity(viewDetails);
                }

                // workaround to avoid issues with some emulators and keyboard devices firing twice if a keyboard enter is used
                // see https://code.google.com/p/android/issues/detail?id=24599
                searchView.clearFocus();

                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        SpannableString s = new SpannableString("Flixster");
        com.claireshu.flickster.TypefaceSpan typeface = new com.claireshu.flickster.TypefaceSpan(this, "Lato-Light.ttf");
        s.setSpan(typeface, 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Update the action bar title with the TypefaceSpan instance
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(s);

        customFont = Typeface.createFromAsset(getAssets(), "fonts/Lato-Light.ttf");


        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchTimelineAsync(0);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        lvMovies = (ListView) findViewById(R.id.lvMovies);
        movies = new ArrayList<>();
        adapter = new MoviesAdapter(this, movies);
        if (lvMovies != null) {
            lvMovies.setAdapter(adapter);
        }

        setUpListViewListener();

        refresh();

        tvMode = (TextView) findViewById(R.id.tvMode);
        tvMode.setTypeface(customFont);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void refresh() {
        toggleMode = !toggleMode;
        if (toggleMode) {
            url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
            TextView tvMode = (TextView) findViewById(R.id.tvMode);
            tvMode.setText(R.string.now_playing);
        } else {
            url = "https://api.themoviedb.org/3/movie/popular?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";
            TextView tvMode = (TextView) findViewById(R.id.tvMode);
            tvMode.setText(R.string.popular_trending);
        }

        client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                JSONArray movieJsonResults = null;
                try {
                    movieJsonResults = response.getJSONArray("results");
                    movies.clear();
                    movies.addAll(Movie.fromJsonArray(movieJsonResults));
                    adapter.notifyDataSetChanged();
                    Log.d("DEBUG", movieJsonResults.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    private void setUpListViewListener() {
        lvMovies.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent viewDetails = new Intent(MovieActivity.this, MovieDetails.class);
                        viewDetails.putExtra("title", movies.get(position).getTitle());
                        viewDetails.putExtra("rating", movies.get(position).getRating());
                        viewDetails.putExtra("popularity", movies.get(position).getPopularity());
                        viewDetails.putExtra("overview", movies.get(position).getOverview());
                        startActivity(viewDetails);
                    }
                }
        );
    }

    public void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // `client` here is an instance of Android Async HTTP
        client.get(url, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;
                try {
                    movieJsonResults = response.getJSONArray("results");
                    movies.clear();
                    movies.addAll(Movie.fromJsonArray(movieJsonResults));
                    adapter.notifyDataSetChanged();
                    //Log.d("DEBUG", movieJsonResults.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Remember to CLEAR OUT old items before appending in the new ones
                adapter.clear();
                // ...the data has come back, add new items to your adapter...
                adapter.addAll(Movie.fromJsonArray(movieJsonResults));
                // Now we call setRefreshing(false) to signal refresh has finished
                swipeContainer.setRefreshing(false);
            }

            public void onFailure(Throwable e) {
                Log.d("DEBUG", "Fetch timeline error: " + e.toString());
            }
        });
    }

    public void onSwitchModes(MenuItem item) {
        refresh();
        adapter.notifyDataSetChanged();
        Log.d("DEBUG", "hello");
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client2.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Movie Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.claireshu.flickster/http/host/path")
        );
        AppIndex.AppIndexApi.start(client2, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Movie Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://com.claireshu.flickster/http/host/path")
        );
        AppIndex.AppIndexApi.end(client2, viewAction);
        client2.disconnect();
    }
}
