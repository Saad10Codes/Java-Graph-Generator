package com.inlimite.drinkcounter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class ViewCount extends Activity
{
	public DatabaseHelper database;
	
	public String[] result;
	public String[] drinks;
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
        setContentView(R.layout.countview);
        
        database = new DatabaseHelper(this);
        
        getValues();
        
	    //get the grid view
	    GridView grid = (GridView) findViewById(R.id.gridView);
        //assign a layout to the grid view
	    grid.setAdapter(new IconicAdapter());
        
	}
	
	private void getValues()
	{
		//initialize and set variables
		drinks = database.getAllDrinks();
		
		result = new String[drinks.length];
		
		//populate the result variable
		for(int i = 0; i < drinks.length; i += 1)
		{
			result[i] = drinks[i] + ": " + database.getCount(drinks[i]);
		}
	}
	
	class IconicAdapter extends ArrayAdapter<String> 
	{
	    IconicAdapter() 
	    {
	    	//pass all parameters to the ArayAdapter
	    	super(ViewCount.this, R.layout.listview, R.id.label, result);
	    }
	    
	    public View getView(int position, View convertView, ViewGroup parent) 
	    {
	    	View row = super.getView(position, convertView, parent);
	    	ImageView icon = (ImageView) row.findViewById(R.id.icon);
	    	
    		//get the image path based on the name of the variable being put on the screen
	    	int path = getResources().getIdentifier(drinks[position].toLowerCase(), "drawable", "com.inlimite.drinkcounter");
	    	
	    	//if a picture was found
	    	if(path != 0)
	    	{
		    	//set the image
		    	icon.setImageResource(path);
	    	}
	    	return(row);
	    }
	}
}

