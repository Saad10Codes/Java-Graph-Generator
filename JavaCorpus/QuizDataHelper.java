package com.cs.uwindsor.group.project;

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

import com.cs.uwindsor.group.project.R;

public class QuizDataHelper {
	
	private MovieDataHelper h;
	public Movie[] movies;
	public TextView question;
	public TextView scoreField;
	public TextView remainingField;
	public RadioButton optA;
	public RadioButton optB;
	public RadioButton optC;
	public RadioButton optD;
	Activity context;
	
	int numRemaining = 5;
	
	String q = "";
	String[] options = new String[4];
	String answer = "A";
	int score = 0;
	
	public QuizDataHelper(Activity c, Movie[] m) {
		context = c;
		movies = m;
		h = new MovieDataHelper(c);
		
		question = (TextView) c.findViewById(R.id.question);
		scoreField = (TextView) c.findViewById(R.id.scoreText);
		remainingField = (TextView) c.findViewById(R.id.remainingText);
		optA = (RadioButton) c.findViewById(R.id.optiona);
		optB = (RadioButton) c.findViewById(R.id.optionb);
		optC = (RadioButton) c.findViewById(R.id.optionc);
		optD = (RadioButton) c.findViewById(R.id.optiond);
	}
	
public void submitAnswer(View v) {
		
		--numRemaining;
		boolean right = checkAnswer();
		score = right ? ++score : score;
		scoreField.setText("Score: " + String.valueOf(score));
		remainingField.setText("Questions Remaining: " + String.valueOf(numRemaining));
		if(numRemaining > 0) {
			String status = right ? "Correct!" : "Sorry, that answer is incorrect.";
			final AlertDialog a1 = new AlertDialog.Builder(context).create();
			a1.setTitle(status);
			a1.setButton("OK", new DialogInterface.OnClickListener() {
			
				@Override
				public void onClick(DialogInterface dialog, int which) {
					setupQuestion();
				}
			});
			a1.show();
			optA.setChecked(false);
			optB.setChecked(false);
			optC.setChecked(false);
			optD.setChecked(false);
		} else {
				if(h.isScoreOnBoard(score)) {
					AlertDialog.Builder alert = new AlertDialog.Builder(context);  
	
			       alert.setTitle("Title");  
			       alert.setMessage("Your score of " + score + " is a Top 5 High Score! Enter your name:");  
	
			       EditText nameEdit = new EditText(context);
			       //InputFilter[] FilterArray = new InputFilter[1];
			       //FilterArray[0] = new InputFilter.LengthFilter(10);
			       //nameEdit.setFilters(FilterArray);
			       final EditText input = new EditText(context);  
			       alert.setView(input);  
	
			       final Context that = v.getContext();
			       alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
			       public void onClick(DialogInterface dialog, int whichButton) {  
				       String name = input.getText().toString(); 
				       if(name.length() > 0) {
				    	   h.updateScores(score, name);
						   that.startActivity(new Intent(that,HighScores.class));
				       }
	
			         }  
			       });  
	
			       alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
			       public void onClick(DialogInterface dialog, int whichButton) {  
	
			           return;
			         }  
			      });  
	
			      alert.show();
				} else {
					final AlertDialog al = new AlertDialog.Builder(v.getContext()).create();
					al.setTitle("You have completed the quiz. Your score of " + score + " did not make the Top 5 High Scores.");
					al.setButton("OK", new DialogInterface.OnClickListener() {
					
						@Override
						public void onClick(DialogInterface dialog, int which) {
							return;
						}
					});
					al.show();
					v.getContext().startActivity(new Intent(v.getContext(),HighScores.class));
				}
		}
		
	}

public boolean checkAnswer(){
	
	TextView scoreField = (TextView) context.findViewById(R.id.scoreText);
	RadioButton optA = (RadioButton) context.findViewById(R.id.optiona);
	RadioButton optB = (RadioButton) context.findViewById(R.id.optionb);
	RadioButton optC = (RadioButton) context.findViewById(R.id.optionc);
	RadioButton optD = (RadioButton) context.findViewById(R.id.optiond);
	TextView remainingField = (TextView) context.findViewById(R.id.remainingText);
	
	if(optA.isChecked() && answer.equals("A")) {
		return true;
	}
	else if(optB.isChecked() && answer.equals("B")) {
		return true;
	}
	else if(optC.isChecked() && answer.equals("C")) {
		return true;
	}
	else if(optD.isChecked() && answer.equals("D")) {
		return true;
	}
	return false;
}

public void setupQuestion () {
	if(numRemaining < 1) {
		Intent i = new Intent();
		i.setClass(context, MainActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		context.startActivity(i);
	} else {
		Random movieSelector = new Random();
		int movienum = movieSelector.nextInt(movies.length);
		int answernum = movieSelector.nextInt(4);
		int qtypenum = movieSelector.nextInt(4);
		
		if(qtypenum == 0) { //Ask about year of release
			q = "What year was " + movies[movienum].title + " released?";
			options[answernum] = movies[movienum].released.substring(0, 4);
			int yearnum = Integer.parseInt(options[answernum]);
			for(int i=0; i < options.length; i++){
				if (i != answernum) {
					++yearnum;
					options[i] = String.valueOf(yearnum);
				}
			}
		} else if (qtypenum == 1) { //Ask about title
			q = "Which of the following is a movie on your list?";
			options[answernum] = movies[movienum].title;
			for(int i=0; i < options.length; i++){
				if  (i != answernum && i == 0) {
					options[i] = "Kickpuncher III";
				} else if (i != answernum && i == 1) {
					options[i] = "Return Of A Person";
				} else if (i != answernum && i == 2) {
					options[i] = "Night Of The Day";
				} else if (i != answernum && i == 3) {
					options[i] = "Beans As A Snack";
				}
			}
		} else if (qtypenum == 2) { //Ask about original name
			q = "What is the original name of " + movies[movienum].title +"?";
			options[answernum] = movies[movienum].originalname;
			for(int i=0; i < options.length; i++){
				if (i != answernum && i == 0) {
					options[i] = "None Of These";
				} else if (i != answernum && i == 1) {
					options[i] = "The Film Was Unnamed";
				} else if (i != answernum && i == 2) {
					options[i] = "KickPuncher 3";
				} else if (i != answernum && i == 3) {
					options[i] = "Tony Clifton: The Movie";
				}
			} 
		} else if (qtypenum == 3) { //Ask about title
			q = "Which of the following is a movie on your list?";
			options[answernum] = movies[movienum].title;
			for(int i=0; i < options.length; i++){
				if  (i != answernum && i == 0) {
					options[i] = "Breaking Good";
				} else if (i != answernum && i == 1) {
					options[i] = "I Am Bob";
				} else if (i != answernum && i == 2) {
					options[i] = "Spinning To Light";
				} else if (i != answernum && i == 3) {
					options[i] = "Google vs Bing";
				}
			}
		} 
		
		question.setText(q);
		optA.setText("A) " + options[0]);
		optB.setText("B) " + options[1]);
		optC.setText("C) " + options[2]);
		optD.setText("D) " + options[3]);
		if(answernum == 0) {
			answer = "A";
		} else if(answernum == 1) {
			answer = "B";
		} else if(answernum == 2) {
			answer = "C";
		} else if(answernum == 3) {
			answer = "D";
		}
	}
}

public void close(){
	if (h != null) {
		h.close();
		h = null;
	}
}

}
