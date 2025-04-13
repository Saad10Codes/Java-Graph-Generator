package com.cs.uwindsor.group.project;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.EditText;

import java.util.Random;

public class Quiz extends Activity {
	public Context that;
	
	public Movie[] movies;
	
	QuizDataHelper q;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (getIntent() == null
				|| getIntent().getSerializableExtra("movie_list") == null) {
			finish();
			startActivity(new Intent(that,MainActivity.class));
		} else {
			setContentView(R.layout.quiz);
			that = this;
			
			Object[] o = (Object[]) getIntent().getSerializableExtra(
			"movie_list");
			movies = new Movie[o.length];
			int i = 0;
			for (Object t : o) {
				if (t instanceof Movie) {
					movies[i] = (Movie) t;
					i++;
				}
			}
			
			if(movies.length > 0) {
 			
				q = new QuizDataHelper(this, movies);
				setupQuestion(); //Set up the question
			} else {
				final AlertDialog a = new AlertDialog.Builder(Quiz.this).create();
				a.setTitle("To do a quiz, you must have at least" +
						" one movie in your movie list.");
				a.setButton("OK", new DialogInterface.OnClickListener() {
				
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
				a.show();
			}
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		q.close();
	}
	
	
	public void submitAnswer(View view) {
		q.submitAnswer(view);
	}
	
	public void setupQuestion () {
		q.setupQuestion();
	}
	
}
