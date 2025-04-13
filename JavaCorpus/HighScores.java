package com.cs.uwindsor.group.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class HighScores extends Activity {
	
	public Context that;
	private HighScoresDataHelper h;
	
	private TextView score1;
	private TextView score2;
	private TextView score3;
	private TextView score4;
	private TextView score5;
	
	private TextView name1;
	private TextView name2;
	private TextView name3;
	private TextView name4;
	private TextView name5;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.scores);
		that = this;
		
		h = new HighScoresDataHelper(this);
		
		name1 = (TextView) this.findViewById(R.id.scoreName1);
		name2 = (TextView) this.findViewById(R.id.scoreName2);
		name3 = (TextView) this.findViewById(R.id.scoreName3);
		name4 = (TextView) this.findViewById(R.id.scoreName4);
		name5 = (TextView) this.findViewById(R.id.scoreName5);
		
		score1 = (TextView) this.findViewById(R.id.score1);
		score2 = (TextView) this.findViewById(R.id.score2);
		score3 = (TextView) this.findViewById(R.id.score3);
		score4 = (TextView) this.findViewById(R.id.score4);
		score5 = (TextView) this.findViewById(R.id.score5);
		
		updateScores();
			
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		h.close();
	}
	
	public void updateScores(){
		h.updateScores();
	}
	
	public void resetScores(View view) {
		h.resetScores(view);
	}
	
	public void emailScores(View view) {
		h.emailScores(view);
	}

}
