package com.inlimite.drinkcounter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper
{
	private static final String DBNAME = "DrinkCounterDatabase";
	private static final int DBVERSION = 1;
	
	private static final String COLID = "id";
	private static final String COLDRINK = "drink";
	private static final String COLCOUNT = "count";
	
	private static final String TABLENAME = "DrinkCount";
	private final String SQLNEW = "CREATE TABLE IF NOT EXISTS " + TABLENAME + 
								" ( " + COLID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
								COLDRINK + " TEXT, " + COLCOUNT + " INTEGER )";
	private static final String SQLDELETE = "DROP TABLE IF EXISTS " + TABLENAME;
		
	public DatabaseHelper(Context context) 
	{
		//pass variables to the parent
		super(context, DBNAME+".db", null, DBVERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		//create new table
		db.execSQL(SQLNEW);
		//ad default values to a fresh installation
		db.execSQL("INSERT INTO "+TABLENAME+" ("+COLDRINK+", "+COLCOUNT+") VALUES (\'Coffee\', 0)");
		db.execSQL("INSERT INTO "+TABLENAME+" ("+COLDRINK+", "+COLCOUNT+") VALUES (\'Tea\', 0)");
		db.execSQL("INSERT INTO "+TABLENAME+" ("+COLDRINK+", "+COLCOUNT+") VALUES (\'Water\', 0)");
		db.execSQL("INSERT INTO "+TABLENAME+" ("+COLDRINK+", "+COLCOUNT+") VALUES (\'Milk\', 0)");
		db.execSQL("INSERT INTO "+TABLENAME+" ("+COLDRINK+", "+COLCOUNT+") VALUES (\'Pop\', 0)");
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		//drop old table
		db.execSQL(SQLDELETE);
		//create new one
		onCreate(db);
	}
	
	private void addDefaultDrinks()
	{
		this.addDrink("Coffee");
		this.addDrink("Tea");
		this.addDrink("Water");
		this.addDrink("Milk");
		this.addDrink("Pop");
	}
	
	public void restoreDefaultDrinks()
	{
		this.deleteAllDrinks();
		this.addDefaultDrinks();
	}
	
	public boolean addDrink(String drinkName)
	{
		//check if there is a drink with that name already
		if(!findDrink(drinkName))
		{
			SQLiteDatabase database = getWritableDatabase();

			ContentValues values = new ContentValues();
			values.put(COLDRINK, drinkName);
			values.put(COLCOUNT, 0);
			
			//insert the new drink into the database
			database.insert(TABLENAME, null, values);
			
			database.close();
			
			//if no drink is found, created new one
			return true;
		}
		
		//drink is already in database, return false
		return false;
	}
	
	public boolean findDrink(String drinkName)
	{
		
		try
		{
			SQLiteDatabase database = getReadableDatabase();

			//get the number of drinks with "drinkName" 
			Cursor cur = database.rawQuery("SELECT COUNT(*) FROM " + TABLENAME + " WHERE drink=\'" + drinkName + "\'", null);
			
			if(cur != null)
			{
				//go to the first row
				cur.moveToFirst();
				if(cur.getInt(0) > 0) // if there is a drink already with the same name
				{
					return true;
				}
			}

			database.close();
		}
		catch(SQLiteException e)
		{
		}
		
		//if there is no drink with the same name
		return false;
	}
	
	public void deleteDrink(String drinkName)
	{
		SQLiteDatabase database = getWritableDatabase();

		//clear row that has the drink
		database.delete(TABLENAME, "drink=\'" + drinkName + "\'", null);
		
		database.close();
	}
	
	public void deleteAllDrinks()
	{
		SQLiteDatabase database = getWritableDatabase();
		
		//clear all rows from table
		database.delete(TABLENAME, null, null);
		
		database.close();
	}
	
	public void resetAllCounts()
	{
		SQLiteDatabase database = getWritableDatabase();
		
		//reset all counts to 0
		String sql = "UPDATE " + TABLENAME + " SET count=\'0\'";
		database.execSQL(sql);
		
		database.close();
	}
	
	public void incrementCount(String drinkName)
	{
		//if the drink exists
		if(findDrink(drinkName))
		{
			//get the current count
			int count = getCount(drinkName);
			
			//update the values
			ContentValues values = new ContentValues();
			values.put(COLCOUNT, count+1);

			try
			{
				//open writable database
				SQLiteDatabase database = getWritableDatabase();
				
				//commit the changes on the database
				database.update(TABLENAME, values, COLDRINK+"=\'"+drinkName+"\'", null);
				
				database.close();
			}
			catch(SQLiteException e)
			{
				
			}
		}
	}
	
	public int getCount(String drinkName)
	{
		int count = 0;
		
		//if the drink exists
		if(findDrink(drinkName))
		{
			try
			{
				SQLiteDatabase database = getReadableDatabase();
				
				//get a result from the database
				Cursor cursor = database.rawQuery("SELECT "+COLCOUNT+" FROM "+TABLENAME+" WHERE "+COLDRINK+"=\'"+drinkName+"\'", null);
				
				//if there is a result
				if(cursor != null)
				{					
					//go to the column and retrieve data
					cursor.moveToFirst();
					count = cursor.getInt(0);
				}

				database.close();
			}
			catch(SQLiteException e)
			{
			}
		}
		
		return count;
	}
	
	public String[] getAllDrinks() throws SQLiteException
	{
		Cursor cur;
		String[] result = null;

		try
		{
			SQLiteDatabase database = getReadableDatabase();
			
			//run the query
			cur = database.rawQuery("SELECT "+COLDRINK+" FROM "+TABLENAME, null);
			
			//initialize variable
			result = new String[cur.getCount()];
			
			//go through data and retrieve the name of drinks
			if(cur.moveToFirst())
			{
				for(int i = 0; i < result.length; i += 1)
				{
					result[i] = cur.getString(cur.getColumnIndex(COLDRINK));
					cur.moveToNext();
				}
			}

			database.close();
		}
		catch(SQLiteException e)
		{
		}
		
		return result;
	}
	
}
