package com.claireshu.flickster;

import android.content.Context;
import android.content.res.Configuration;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.claireshu.flickster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by claireshu on 6/15/16.
 */
public class MoviesAdapter extends ArrayAdapter<Movie> {
    private static class ViewHolder {
        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;
    }

    public MoviesAdapter (Context context, ArrayList<Movie> movies) {
        super(context, R.layout.item_movie, movies);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Movie movie = getItem(position);

        ViewHolder viewHolder;

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.tvOverview = (TextView) convertView.findViewById(R.id.tvOverview);
            viewHolder.ivPoster = (ImageView) convertView.findViewById(R.id.ivPoster);

            convertView.setTag(viewHolder);
        }  else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Populate the data into the template view using the data object
        viewHolder.tvTitle.setText(movie.getTitle());
        viewHolder.tvOverview.setText(movie.getOverview());
        String imageUri = null;
        Context context = getContext();
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            imageUri = movie.getBackdropUrl();
            Picasso.with(getContext()).load(imageUri)
                    .placeholder(R.drawable.land_placeholder)
                    .transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivPoster);
        } else if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            imageUri = movie.getPosterUrl();
            Picasso.with(getContext()).load(imageUri)
                    .placeholder(R.drawable.portrait_placeholder)
                    .transform(new RoundedCornersTransformation(10, 10)).into(viewHolder.ivPoster);
        }

        //Log.d("MoviesAdapter", "Position:" + position);

        // Return the completed view to render on screen
        return convertView;
    }
}
