package com.claireshu.flickster;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        int position = getIntent().getIntExtra("position", 0);
        String title = getIntent().getStringExtra("title");
        String overview = getIntent().getStringExtra("overview");
        int rating = getIntent().getIntExtra("rating", 0);
        int popularity = getIntent().getIntExtra("popularity", 0);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(title);

        rbRating = (RatingBar) findViewById(R.id.rbRating);
        rbRating.setRating(rating/2);

        pbPopularity = (ProgressBar) findViewById(R.id.pbPopularity);
        pbPopularity.setProgress(popularity);

        tvOverview = (TextView) findViewById(R.id.tvOverview);
        tvOverview.setText(overview);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // This is the up button
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
}
