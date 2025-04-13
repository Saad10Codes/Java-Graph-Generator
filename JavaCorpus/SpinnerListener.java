package com.inlimite.drinkcounter;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SpinnerListener implements OnItemSelectedListener 
{
	protected String positionName;
	
	//listen to what the user selects in the spinner
	public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) 
	{
		positionName = parent.getItemAtPosition(pos).toString();
	}
	
	public void onNothingSelected(AdapterView<?> parent) 
	{
		//Do nothing
	}
	
	public String getSelection()
	{
		return positionName;
	}
	
}
