package com.cs.uwindsor.group.project.utils;

import java.net.URLEncoder;

import com.cs.uwindsor.group.project.MainActivity;
import com.cs.uwindsor.group.project.Movie;
import com.cs.uwindsor.group.project.MovieDataHelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.RatingBar;


public class MovieDetailsUtils {
	
	public static void saveMovie(View view, Movie movie) {
	
		MovieDataHelper h = new MovieDataHelper(view.getContext());
		h.insert(movie.id);
		h.close();

		final Context context = view.getContext();
		
		final AlertDialog a = new AlertDialog.Builder(view.getContext()).create();
		a.setTitle("The movie has been saved.");
		a.setButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick (DialogInterface dialog, int which) {

				Intent i = new Intent();
				i.setClass(context, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(i);
			}
		});
		a.show();
	}
	
	public static void deleteMovie (View view, Movie movie) {

		MovieDataHelper h = new MovieDataHelper(view.getContext());
		h.delete(movie.id);
		h.close();
		
		final Context c = view.getContext();
		
		AlertDialog a = new AlertDialog.Builder(view.getContext()).create();
		a.setTitle("The movie has been deleted.");
		a.setButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick (DialogInterface dialog, int which) {

				Intent i = new Intent();
				i.setClass(c, MainActivity.class);
				i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				c.startActivity(i);
			}
		});
		a.show();
	}

	public static void buyMovie (View view, Movie movie) {
		
		String released = "";
		if (movie.released != null) {
			released = movie.released.substring(0, 4);
		}

		String url = "http://www.amazon.com/s/ref=nb_sb_noss?url=search-alias%3Dmovies-tv&field-keywords=";
		url += URLEncoder.encode(movie.title + " " + released + "");
		url += "&x=0&y=0";

		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		view.getContext().startActivity(browserIntent);
	}
	
	public static void changeRating (Context c, Movie movie, RatingBar ratingBar, float rating, boolean fromTouch) {

		float rate = ratingBar.getRating();
		
		MovieDataHelper h = new MovieDataHelper(c);
		
		h.updateRating(movie.id, rate);
		movie.rating = rate;
		
		h.close();
	}
}
