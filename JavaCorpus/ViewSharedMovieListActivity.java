package com.cs.uwindsor.group.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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

import com.cs.uwindsor.group.project.adapters.LazyPublicListAdapter;
import com.cs.uwindsor.group.project.http.AsyncHttpClient;
import com.cs.uwindsor.group.project.http.AsyncHttpResponseHandler;
import com.cs.uwindsor.group.project.utils.SharedMovieUtils;

// Do NOT start without passing an intent
public class ViewSharedMovieListActivity extends Activity {

	LazyPublicListAdapter adapter;
	public SharedMovie[] entries;
	public static Activity a;

	public void onCreate (Bundle icicle) {

		super.onCreate(icicle);

		if (getIntent() == null || getIntent().getSerializableExtra("user_list") == null) {
			finish();
		} else {
			setContentView(R.layout.userlistcontainer);

			a = this;

			ActionBar actionBar = this.getActionBar();
			actionBar.setDisplayHomeAsUpEnabled(true);

			Object[] o = (Object[]) getIntent().getSerializableExtra("user_list");
			entries = new SharedMovie[o.length];
			int i = 0;
			for (Object t : o) {
				if (t instanceof SharedMovie) {
					entries[i] = (SharedMovie) t;
					i++;
				}
			}
			
			ListView list = (ListView) findViewById(R.id.userlistview);
			
			adapter = new LazyPublicListAdapter(this, entries);
			
			list.setAdapter(adapter);
			list.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick (AdapterView<?> arg0, View arg1, int arg2, long arg3) {

					SharedMovie m = entries[arg2];
					SharedMovieUtils.startViewSharedMovieListActivity(a, m);
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