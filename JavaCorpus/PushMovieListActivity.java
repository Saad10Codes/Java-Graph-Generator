package com.cs.uwindsor.group.project;

import com.cs.uwindsor.group.project.utils.SharedMovieUtils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class PushMovieListActivity extends Activity {

	EditText t;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.pushlist);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	public void push_lists(View view) {
		
		SharedMovieUtils.pushList(this);
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
