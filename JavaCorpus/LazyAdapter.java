package com.cs.uwindsor.group.project.adapters;

import com.cs.uwindsor.group.project.Movie;
import com.cs.uwindsor.group.project.R;
import com.cs.uwindsor.group.project.R.id;
import com.cs.uwindsor.group.project.R.layout;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LazyAdapter extends BaseAdapter {

	private Activity activity;
	private Movie[] data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public LazyAdapter(Activity a, Movie[] d) {
		activity = a;
		data = d;
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount() {
		return data.length;
	}

	public Object getItem(int position) {
		return position;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.rows2, null);

		TextView text = (TextView) vi.findViewById(R.id.movie_title);
		text.setText(data[position].title);

		text = (TextView) vi.findViewById(R.id.movie_rating);
		text.setText(String.valueOf("Rating: " + data[position].rating) + "/5.0");

		text = (TextView) vi.findViewById(R.id.movie_release);
		text.setText(String.valueOf("Released " + data[position].released));

		text = (TextView) vi.findViewById(R.id.movie_overview);
		text.setText(String.valueOf(data[position].overview));

		ImageView image = (ImageView) vi.findViewById(R.id.movie_image);
		imageLoader.DisplayImage(data[position].thumb, activity, image);
		return vi;
	}
}