package com.inlimite.drinkcounter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditMenu extends Activity
{
	protected DatabaseHelper database;
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.editdrinks);
        
        LinearLayout focus = (LinearLayout) findViewById(R.id.Focus);
        focus.requestFocus();
        
        //connect to the database
        database = new DatabaseHelper(this);
                
        //create the list of drinks to be deleted.
        createDrinkList();
    }
    
    @Override
    public void onPause()
    {
    	super.onPause();
    	
    	database.close();
    }
    
    public void createDrinkList()
    {
    	ListView deleteList = (ListView) findViewById(R.id.ListViewDrinks);
        String[] drinks = database.getAllDrinks();
        
    	//Create array of string to ad into the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.menulist, R.id.list, drinks); 
        //add array to spinner
        deleteList.setAdapter(adapter);    	
    }
    
    public void deleteItem(View view)
    {
    	LinearLayout row = (LinearLayout) view.getParent();
    	TextView drink = (TextView) row.getChildAt(0);
    	
    	database.deleteDrink((String) drink.getText());
    	
    	updateScreen();
    }
    
    private void updateScreen()
    {
    	finish();
    	startActivity(getIntent());
    }
    
    public void addNewDrink(View view)
    {    	
    	EditText text = (EditText) findViewById(R.id.FieldNewDrink);
    	
    	if(!database.findDrink(text.getText().toString()))
    	{
    		database.addDrink(text.getText().toString());
	    	//notify the user of action
		    Toast.makeText(this, text.getText().toString() + " has been added to the database.", Toast.LENGTH_SHORT).show();
		    
		    updateScreen();
    	}
    	else 
    	{
    		Toast.makeText(this, text.getText().toString() + " is already in the database.", Toast.LENGTH_SHORT).show();
    	}
    }

    public void resetAll(View view)
	{		
		database.resetAllCounts();
		
		//notify the user of action
	    Toast.makeText(this, "All counts cleared.", Toast.LENGTH_SHORT).show();
	    
	    updateScreen();
	}
	
	public void clearAll(View view)
	{
		database.deleteAllDrinks();
		
		//notify the user of action
	    Toast.makeText(this, "All data has been removed.", Toast.LENGTH_SHORT).show();
	    
	    updateScreen();
	}
	
	public void restoreDefault(View view)
	{
		database.restoreDefaultDrinks();
		
		//notify the user of action
	    Toast.makeText(this, "Drinks have been restored to default.", Toast.LENGTH_SHORT).show();
	    
	    updateScreen();
	}
}
