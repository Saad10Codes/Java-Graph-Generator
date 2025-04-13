package com.cs.uwindsor.group.project;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Calendar;

import com.cs.uwindsor.group.project.adapters.ImageLoader;
import com.cs.uwindsor.group.project.utils.MovieDetailsUtils;
import com.cs.uwindsor.group.project.utils.Showtimes;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

// MUST be created with an Intent with a extra of "movie";
public class MovieDetails extends Activity implements RatingBar.OnRatingBarChangeListener {

	public Movie movie;
	public static Activity a;
	public ImageLoader imageLoader;
	public RatingBar ratebar;

	@Override
	public void onCreate (Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		if (getIntent() == null || getIntent().getSerializableExtra("movie") == null) {
			finish();
		} else {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.movie_details);

			a = this;

			ActionBar actionBar = this.getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);

			Object o = (Object) getIntent().getSerializableExtra("movie");
			if (o instanceof Movie)
				movie = (Movie) o;
			else
				finish();

			actionBar.setTitle(movie.title);

			TextView text = (TextView) findViewById(R.id.title);
			text.setText(movie.title);

			text = (TextView) findViewById(R.id.year);
			text.setText(String.valueOf(movie.released));

			text = (TextView) findViewById(R.id.plot);
			text.setText(String.valueOf(movie.overview));

			text = (TextView) findViewById(R.id.showtimes);
			String showtimes = Showtimes.getShowtime(movie.title);

			if (showtimes != null) {
				text.setText(showtimes);
			} else {
				text.setText("Showtimes Today: \n\nMovie is not playing today.");
			}

			imageLoader = new ImageLoader(this.getApplicationContext());
			ImageView image = (ImageView) findViewById(R.id.poster);
			imageLoader.DisplayImage(movie.thumb, a, image);

			ratebar = (RatingBar) findViewById(R.id.ratingBar1);
			ratebar.setRating((float) movie.rating);

			// The different rating bars in the layout. Assign the listener to us.
			ratebar.setOnRatingBarChangeListener(this);
		}

	}

	@Override
	public void onDestroy () {

		super.onDestroy();
		a = null;
	}

	@Override
	public void onResume () {

		super.onResume();
		buttonVisibility();
	}

	@Override
	public void onPause () {

		super.onPause();
	}

	public void saveMovie (View view) {

		MovieDetailsUtils.saveMovie(view, movie);
	}

	public void deleteMovie (View view) {

		MovieDetailsUtils.deleteMovie(view, movie);
	}

	
	// view related logic
	private void buttonVisibility () {

		MovieDataHelper h = new MovieDataHelper(this);
		
		if (h.has(movie.id)) {
			Button b = (Button) findViewById(R.id.save_button);
			b.setVisibility(View.INVISIBLE);
			b = (Button) findViewById(R.id.delete_button);
			b.setVisibility(View.VISIBLE);
			ratebar.setVisibility(View.VISIBLE);
		} else {
			Button b = (Button) findViewById(R.id.delete_button);
			b.setVisibility(View.INVISIBLE);
			b = (Button) findViewById(R.id.save_button);
			b.setVisibility(View.VISIBLE);
			ratebar.setVisibility(View.INVISIBLE);

		}

		if (movie.trailer == null || !movie.trailer.contains("http://")) {
			Button b = (Button) findViewById(R.id.view_trailer);
			b.setVisibility(View.INVISIBLE);
		}

		if (movie.released != null) {
			try {
				Calendar c1 = Calendar.getInstance();
				c1.set(Calendar.YEAR, new Integer((String) movie.released.subSequence(0, 4)));
				c1.set(Calendar.MONTH, new Integer((String) movie.released.subSequence(5, 7)));
				c1.set(Calendar.DATE, new Integer((String) movie.released.subSequence(8, 10)));

				Calendar c2 = Calendar.getInstance();
				c2.setTimeInMillis(System.currentTimeMillis() - (1000 * 60 * 60 * 24 * 180));

				if (!c1.before(c2)) {
					Button b = (Button) findViewById(R.id.buy_movie);
					b.setVisibility(View.INVISIBLE);
				}
			} catch (Exception e) {

				Button b = (Button) findViewById(R.id.buy_movie);
				b.setVisibility(View.INVISIBLE);
			}

		}
		
		h.close();
	}

	public void viewTrailer (View view) {

		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(movie.trailer));
		startActivity(browserIntent);
	}

	public void buyMovie (View view) {
		
		MovieDetailsUtils.buyMovie(view, movie);
	}

	public void onRatingChanged (RatingBar ratingBar, float rating, boolean fromTouch) {

		Log.i("Test", "Changed");
		MovieDetailsUtils.changeRating(this, movie, ratingBar, rating, fromTouch);
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			Intent i = new Intent();
			i.setClass(getApplicationContext(), MainActivity.class);
			i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

}
