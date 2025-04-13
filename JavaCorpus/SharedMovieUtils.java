package com.cs.uwindsor.group.project.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.cs.uwindsor.group.project.Movie;
import com.cs.uwindsor.group.project.MovieDataHelper;
import com.cs.uwindsor.group.project.MovieList;
import com.cs.uwindsor.group.project.R;
import com.cs.uwindsor.group.project.SharedMovie;
import com.cs.uwindsor.group.project.ViewSharedMovieListActivity;
import com.cs.uwindsor.group.project.R.id;
import com.cs.uwindsor.group.project.http.AsyncHttpClient;
import com.cs.uwindsor.group.project.http.AsyncHttpResponseHandler;
import com.cs.uwindsor.group.project.http.RequestParams;

public class SharedMovieUtils {

	public static void pushList (Activity context) {

		final Activity a = context;

		EditText t = ((EditText) a.findViewById(R.id.movielist_title));
		final MovieDataHelper h = new MovieDataHelper(context);
		
		if (t.getText().toString().trim().length() > 0 &&  (h.generateUserList() != null || !h.generateUserList().equals(""))) {
			

			AsyncHttpClient client = new AsyncHttpClient();
			RequestParams params = new RequestParams();

			params.put("data",
					"[{\"name\":\"" + t.getText().toString().trim() + "\", \"list\":\"" + h.generateUserList() + "\"}]");
			String url = "https://bibim.ly/311/movielist.php";

			client.get(context, url, params, new AsyncHttpResponseHandler() {

				@Override
				public void onSuccess (String response) {

					Log.i("SharedMovies", response);
					Toast toast = Toast.makeText(a, "Movie List Published!", Toast.LENGTH_LONG);
					toast.show();
					a.finish();
				}

				@Override
				public void onFailure (Throwable t) {

					Toast toast = Toast.makeText(a, "Failed to submit! Check internet connection.", Toast.LENGTH_LONG);
					toast.show();
				}

				@Override
				public void onFinish () {

					h.close();
				}
			});
		} else if ((h.generateUserList() == null || h.generateUserList().equals(""))) {
			
			Toast toast = Toast.makeText(a, "Your list must contain movies!", Toast.LENGTH_LONG);
			toast.show();
			
		} else {
			Toast toast = Toast.makeText(a, "You must enter a name for your movie list!", Toast.LENGTH_LONG);
			toast.show();
		}
	}

	public static void getLists (Activity context) {

		final Context c = context;

		AsyncHttpClient client = new AsyncHttpClient();

		String url = "https://bibim.ly/311/movielist.php";

		client.get(context, url, null, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess (String response) {

				ObjectMapper mapper = new ObjectMapper();
				SharedMovie[] s = new SharedMovie[0];

				try {
					s = mapper.readValue(response, SharedMovie[].class);
				} catch (Exception e) {
					e.printStackTrace();
				}

				Intent i = new Intent(c, ViewSharedMovieListActivity.class);
				i.putExtra("user_list", s);
				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				c.startActivity(i);
			}

			@Override
			public void onFailure (Throwable t) {

			}

			@Override
			public void onFinish () {

				// h.close();
			}
		});
	}

	public static void startViewSharedMovieListActivity (Context a, SharedMovie m) {

		final String[] ids = m.list.split(",");

		final MovieDataHelper h = new MovieDataHelper(a);
		final Context context = a;

		ArrayList<Movie> al = new ArrayList<Movie>();
		for (String id : ids) {

			Movie movieSearch = new Movie();
			movieSearch.id = Integer.parseInt((id.split(":")[0]).trim());
			MovieListUtils.handleSelection(a, movieSearch, false);
		}

		Thread thr = new Thread() {

			public void run () {

				try {
				while (MovieListUtils.finished != 0) {
					try {
						sleep(50);
					} catch (InterruptedException e) {
						break;
					}
				}

				ArrayList<Movie> al = new ArrayList<Movie>();
				for (String id : ids) {
					Movie mov = h.select(Integer.parseInt((id.split(":")[0]).trim()));
					mov.rating = Double.valueOf((id.split(":")[1]));

					if (mov.title == null) {
						throw new Exception("Requires internet");
					}
					
					al.add(mov);
				}

				Comparator<Movie> c = new Comparator<Movie>() {

					@Override
					public int compare (Movie object1, Movie object2) {

						if (object1.title != null && object2.title != null)
							return object1.title.compareTo(object2.title);
						else
							return 0;
					}
				};

				Collections.sort(al, c);

				Movie[] movies = al.toArray(new Movie[0]);

				Intent i = new Intent(context, MovieList.class);
				i.putExtra("movie_list", movies);
				i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(i);

				
				} catch (Exception e) {

				} finally {
					h.close();
				}
			}
		};
		thr.start();

	}
}
