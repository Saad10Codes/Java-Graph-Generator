package com.cs.uwindsor.group.project.utils;

import org.codehaus.jackson.map.ObjectMapper;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.cs.uwindsor.group.project.Movie;
import com.cs.uwindsor.group.project.MovieDataHelper;
import com.cs.uwindsor.group.project.MovieDetails;
import com.cs.uwindsor.group.project.http.AsyncHttpClient;
import com.cs.uwindsor.group.project.http.AsyncHttpResponseHandler;

public class MovieListUtils {

	public static volatile int finished = 0;
	
	public static void handleSelection (Context context, Movie movie, boolean startActivity) {

		if (startActivity == false) 
			++finished;
		
		final boolean startWhenDone = startActivity;
		final Context that = context;
		MovieDataHelper h = new MovieDataHelper(that);
		Movie m = h.select(movie.id);
		m.rating = movie.rating;
		m.id = movie.id;
		h.close();
		
		if (m.trailer == null || m.trailer.trim().equals("null")) {

			AsyncHttpClient client = new AsyncHttpClient();

			Toast toast = Toast.makeText(that, "Getting additional movie data. Please wait", Toast.LENGTH_LONG);
			toast.show();

			final String apiKey = "1745f1e65400eac28b2c0d0cecf25f71";
			String url = "http://api.themoviedb.org/2.1/Movie.getInfo/en-US/json/" + apiKey + "/" + m.id;
			client.get(that, url, null, new AsyncHttpResponseHandler() {

				private Movie m;
				
				@Override
				public void onSuccess (String response) {

					try {
						ObjectMapper mapper = new ObjectMapper();
						Movie[] movie = new Movie[0];
						movie = mapper.readValue(response, Movie[].class);

						if (movie.length > 0) {
							MovieDataHelper h = new MovieDataHelper(that);

							if (movie[0].trailer == null || movie[0].trailer.trim().equals("null")) {
								movie[0].trailer = "none";
							}

							h.insert(movie[0]);
							h.close();

							m = movie[0];
						}

					} catch (Exception e) {

					}
				}

				@Override
				public void onFailure (Throwable t) {

					
				}

				@Override
				public void onFinish () {

					if (that != null && startWhenDone) {
						viewDetails(that, m);
					}
					
					if (startWhenDone == false) 
						--finished;
				}
			});
		} else {
			if (startWhenDone)
				viewDetails(that, m);
			
			if (startWhenDone == false) 
				--finished;
		}
	}
	
	public static void viewDetails (Context that, Movie m) {

		Intent intent = new Intent(that, MovieDetails.class);
		intent.putExtra("movie", m);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		that.startActivity(intent);
	}
}
