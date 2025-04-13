package com.cs.uwindsor.group.assignment;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class StartQuiz extends Activity {
	
	/** Called when the activity is first created. */
	private SettingManager savedPrefs;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		savedPrefs = new SettingManager(this);
		
		setContentView(R.layout.startquiz);

		// Set up the spinner
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter
				.createFromResource(this, R.array.user_category,
						android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		
		
		TextView userHeight = (TextView)findViewById(R.id.userHeight);
		userHeight = (EditText)findViewById(R.id.userHeight);
		userHeight.setText(savedPrefs.getStringPref(SettingManager.SETTING_USER_HEIGHT));
		userHeight.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
	            savedPrefs.saveStringPref(SettingManager.SETTING_USER_HEIGHT, s.toString());
	        }
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
	    });
		Editable userHeightCursor = ((EditText)findViewById(R.id.userHeight)).getText();
		Selection.setSelection(userHeightCursor, savedPrefs.getStringPref(SettingManager.SETTING_USER_HEIGHT).length());
		
		TextView userWeight = (TextView)findViewById(R.id.userWeight);
		userWeight = (EditText)findViewById(R.id.userWeight);
		userWeight.setText(savedPrefs.getStringPref(SettingManager.SETTING_USER_WEIGHT));
		userWeight.addTextChangedListener(new TextWatcher(){
	        public void afterTextChanged(Editable s) {
	            savedPrefs.saveStringPref(SettingManager.SETTING_USER_WEIGHT, s.toString());
	        }
	        public void beforeTextChanged(CharSequence s, int start, int count, int after){}
	        public void onTextChanged(CharSequence s, int start, int before, int count){}
	    });
		Editable userWeightCursor = ((EditText)findViewById(R.id.userWeight)).getText();
		Selection.setSelection(userWeightCursor, savedPrefs.getStringPref(SettingManager.SETTING_USER_WEIGHT).length());
		
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    ActionBar actionBar = this.getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

}
