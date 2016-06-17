package com.claireshu.flickster;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

public class MovieDetails extends AppCompatActivity {
    TextView tvTitle;
    RatingBar rbRating;
    ProgressBar pbPopularity;
    TextView tvOverview;
    Typeface customFont;
    TextView tvRating;
    TextView tvPopularity;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // sets custom font to lato
        customFont = Typeface.createFromAsset(this.getAssets(), "fonts/Lato-Light.ttf");

        // sets the action bar text font to customFont
        SpannableString s = new SpannableString("Flixster");
        com.claireshu.flickster.TypefaceSpan typeface = new com.claireshu.flickster.TypefaceSpan(this, "Lato-Light.ttf");
        s.setSpan(typeface, 0, s.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(s);

        // stores data received
        String title = getIntent().getStringExtra("title");
        String overview = getIntent().getStringExtra("overview");
        int rating = getIntent().getIntExtra("rating", 0);
        int popularity = getIntent().getIntExtra("popularity", 0);
        id = getIntent().getIntExtra("id", 0);

        // displays and sets all data on screen, sets font to customFont
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        tvTitle.setTypeface(customFont);

        rbRating = (RatingBar) findViewById(R.id.rbRating);
        rbRating.setRating(rating/2);

        pbPopularity = (ProgressBar) findViewById(R.id.pbPopularity);
        pbPopularity.setProgress(popularity);

        tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvOverview.setText(overview);
        tvOverview.setTypeface(customFont);

        tvRating = (TextView) findViewById(R.id.tvRating);
        tvRating.setTypeface(customFont);

        tvPopularity = (TextView) findViewById(R.id.tvPopularity);
        tvPopularity.setTypeface(customFont);

    }

    // handles when the back arrow is pressed to go back to home page
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                // overridePendingTransition(R.animator.anim_left, R.animator.anim_right);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onReturnMain(View view) {
        this.finish();
    }

    public void onWatchVideo(View view) {
        Intent viewVideo = new Intent(MovieDetails.this, MovieVideo.class);
        viewVideo.putExtra("id", id);
        startActivity(viewVideo);
    }
}
