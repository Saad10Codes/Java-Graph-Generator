package com.cs.uwindsor.group.project;

import org.codehaus.jackson.map.ObjectMapper;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.cs.uwindsor.group.project.adapters.LazyAdapter;
import com.cs.uwindsor.group.project.http.AsyncHttpClient;
import com.cs.uwindsor.group.project.http.AsyncHttpResponseHandler;
import com.cs.uwindsor.group.project.utils.MovieListUtils;

// Do NOT start without passing an intent
public class MovieList extends Activity {

	LazyAdapter adapter;
	public Movie[] movies;
	public static Context that;
	Movie m;
	public static Activity a;

	public void onCreate (Bundle icicle) {

		super.onCreate(icicle);

		if (getIntent() == null || getIntent().getSerializableExtra("movie_list") == null) {
			finish();
		} else {
			setContentView(R.layout.movie_list);

			that = this.getApplicationContext();

			a = this;

			ActionBar actionBar = this.getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);

			Object[] o = (Object[]) getIntent().getSerializableExtra("movie_list");
			movies = new Movie[o.length];
			int i = 0;
			for (Object t : o) {
				if (t instanceof Movie) {
					movies[i] = (Movie) t;
					i++;
				}
			}

			ListView list = (ListView) findViewById(R.id.movie_listview);
			adapter = new LazyAdapter(this, movies);
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick (AdapterView<?> arg0, View arg1, int arg2, long arg3) {

					// arg2 is position;
					m = movies[arg2];
					MovieListUtils.handleSelection(that, m, true);
					// Cant be bothered using a real check condition
				}
			});
		}
	}

	@Override
	public void finish () {

		super.finish();
	}

	@Override
	public void onResume () {

		super.onResume();

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