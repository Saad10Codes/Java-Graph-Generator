package com.cs.uwindsor.group.project;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class HighScoresDataHelper {
	Activity context;
	
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
	MovieDataHelper h;
	
	public HighScoresDataHelper(Activity c) {
		context = c;
		h = new MovieDataHelper(c);
		name1 = (TextView) c.findViewById(R.id.scoreName1);
		name2 = (TextView) c.findViewById(R.id.scoreName2);
		name3 = (TextView) c.findViewById(R.id.scoreName3);
		name4 = (TextView) c.findViewById(R.id.scoreName4);
		name5 = (TextView) c.findViewById(R.id.scoreName5);
		
		score1 = (TextView) c.findViewById(R.id.score1);
		score2 = (TextView) c.findViewById(R.id.score2);
		score3 = (TextView) c.findViewById(R.id.score3);
		score4 = (TextView) c.findViewById(R.id.score4);
		score5 = (TextView) c.findViewById(R.id.score5);
		
	}
	
	public void updateScores(){
		String[] names = h.getTopNames();
		if(names[0] != null) {
			name1.setText(names[0]);
			score1.setText(String.valueOf(h.getScore(names[0])));
		} 
		if(names[1] != null) {
			name2.setText(names[1]);
			score2.setText(String.valueOf(h.getScore(names[1])));
		}
		if(names[2] != null) {
			name3.setText(names[2]);
			score3.setText(String.valueOf(h.getScore(names[2])));
		}
		if(names[3] != null) {
			name4.setText(names[3]);
			score4.setText(String.valueOf(h.getScore(names[3])));
		}
		if(names[4] != null) {
			name5.setText(names[4]);
			score5.setText(String.valueOf(h.getScore(names[4])));
		}
	}
	
	public void resetScores(View view) {
		h.resetScores();
		updateScores();
	}
	
	public void emailScores(View view) {
		AlertDialog.Builder alert = new AlertDialog.Builder(context);  
		
       alert.setTitle("Title");  
       alert.setMessage("Please enter the email address to send your scores to:");  

       EditText nameEdit = new EditText(context);

       final EditText input = new EditText(context);  
       alert.setView(input);  

       alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {  
       public void onClick(DialogInterface dialog, int whichButton) { 
    	    String[] names = h.getTopNames();
    	    String email = input.getText().toString(); 
    	    String body = "";
    	    if(names[0] != null) {
    	    	body = body.concat(names[0] + ": " + h.getScore(names[0]) + '\n');
    	    }
    	    if(names[1] != null) {
    	    	body = body.concat(names[1] + ": " + h.getScore(names[1]) + '\n');
    	    }
    	    if(names[2] != null) {
    	    	body = body.concat(names[2] + ": " + h.getScore(names[2]) + '\n');
    	    }
    	    if(names[3] != null) {
    	    	body = body.concat(names[3] + ": " + h.getScore(names[3]) + '\n');
    	    }
    	    if(names[4] != null) {
    	    	body = body.concat(names[4] + ": " + h.getScore(names[4]) + '\n');
    	    }
    	    Intent i = new Intent(Intent.ACTION_SEND);
	   		i.setType("text/plain");
	   		i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
	   		i.putExtra(Intent.EXTRA_SUBJECT, "High Scores");
	   		i.putExtra(Intent.EXTRA_TEXT   , body);
	   		try {
	   		    context.startActivity(Intent.createChooser(i, "Send mail..."));
	   		} catch (android.content.ActivityNotFoundException ex) {
	   		    Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
	   		}

         }  
       });  

       alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {  
       public void onClick(DialogInterface dialog, int whichButton) {  

           return;
         }  
      });  

      alert.show();
      

	}
	
	public void close(){
		if (h != null) {
			h.close();
			h = null;
		}
	}
}
