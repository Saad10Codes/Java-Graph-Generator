package com.cs.uwindsor.group.project.adapters;

import com.cs.uwindsor.group.project.R;
import com.cs.uwindsor.group.project.SharedMovie;
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

public class LazyPublicListAdapter extends BaseAdapter {

	private Activity activity;
	private SharedMovie[] data;
	private static LayoutInflater inflater = null;
	public ImageLoader imageLoader;

	public LazyPublicListAdapter (Activity a, SharedMovie[] d) {

		activity = a;
		data = d;
		inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	public int getCount () {

		return data.length;
	}

	public Object getItem (int position) {

		return position;
	}

	public long getItemId (int position) {

		return position;
	}

	public View getView (int position, View convertView, ViewGroup parent) {

		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.userlist, null);

		TextView text = (TextView) vi.findViewById(R.id.userlist_name);
		text.setText(data[position].name);

		text = (TextView) vi.findViewById(R.id.list_contents);
		int num = (data[position].list.length() > 1) ? (data[position].list.replaceAll("[^,]", "").length() + 1) : 0;
		text.setText("Contains " + ((num == 1) ? "1 movie" : (num + " movies")));

		return vi;
	}
}