package com.cs.uwindsor.group.project;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

public class MovieDataHelper {

	private static final String DATABASE_NAME = "movies6.db";
	private static final int DATABASE_VERSION = 13;
	private static final String TABLE_NAME = "movies";
	private static final String USER_TABLE = "user_movies";
	private static final String SHARED_TABLE = "shared_movies";
	private static final String SCORE_TABLE = "user_score";

	private Context context;
	private SQLiteDatabase db;

	private SQLiteStatement insertStmt;
	private SQLiteStatement userInsert;

	private static final String INSERT = "insert or replace into "
			+ TABLE_NAME
			+ "(id, score, popularity, rating, originalname, title, imdbid, url, overview, released, trailer, `cast`, thumb) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String USER_INSERT = "insert or replace into " + USER_TABLE + " (id) VALUES (?)";

	public MovieDataHelper (Context context) {

		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();
		this.insertStmt = this.db.compileStatement(INSERT);
		this.userInsert = this.db.compileStatement(USER_INSERT);
	}

	public long insert (Movie m) {

		this.insertStmt.bindLong(1, m.id);
		this.insertStmt.bindString(2, m.score + " ");
		this.insertStmt.bindString(3, m.popularity + " ");
		this.insertStmt.bindString(4, m.rating + " ");
		this.insertStmt.bindString(5, m.originalname + " ");
		this.insertStmt.bindString(6, m.title + " ");
		this.insertStmt.bindString(7, m.imdbid + " ");
		this.insertStmt.bindString(8, m.url + " ");
		this.insertStmt.bindString(9, m.overview + " ");
		this.insertStmt.bindString(10, m.released + " ");
		this.insertStmt.bindString(11, m.trailer + " ");
		if (m.cast != null) {
			this.insertStmt.bindString(12, m.cast.toString() + " ");
		} else {
			this.insertStmt.bindString(12, " ");
		}
		this.insertStmt.bindString(13, m.thumb + " ");
		return this.insertStmt.executeInsert();
	}

	public long insert (int id) {

		this.userInsert.bindLong(1, id);
		return this.userInsert.executeInsert();
	}

	public int delete (int id) {

		return this.db.delete(USER_TABLE, "id=?", new String[] { String.valueOf(id) });
	}

	public void close () {

		this.db.close();
	}

	public void deleteAll () {

		this.db.delete(TABLE_NAME, null, null);
	}

	public boolean has (int id) {

		Cursor cursor = this.db.query(USER_TABLE, new String[] { "id" }, "id = ?", new String[] { id + "" }, null,
				null, null);
		return (cursor.getCount() > 0);
	}

	public Movie select (int id) {

		Cursor cursor = this.db.query(TABLE_NAME,
				new String[] { "id", " score", " popularity", " rating", " originalname", " title", " imdbid", " url",
						" overview", " released", " trailer", "`cast`", "thumb" }, "id = ?", new String[] { id + "" },
				null, null, null);
		Movie m = new Movie();
		if (cursor.moveToFirst()) {
			do {
				m.id = cursor.getInt(0);
				m.score = new Double(cursor.getString(1));
				m.popularity = new Double(cursor.getString(2));
				m.rating = new Double(cursor.getString(3));
				m.originalname = cursor.getString(4);
				m.title = cursor.getString(5);
				m.imdbid = cursor.getString(6);
				m.url = cursor.getString(7);
				m.overview = cursor.getString(8);
				m.released = cursor.getString(9);
				m.trailer = cursor.getString(10);
				// m.cast = cursor.getString(11);
				m.thumb = cursor.getString(12);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return m;
	}

	public Movie[] selectAllUser () {

		List<Movie> list = new ArrayList<Movie>();
		Cursor cursor = this.db.query(USER_TABLE, new String[] { "id" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				list.add(select(cursor.getInt(0)));
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}

		Comparator<Movie> c = new Comparator<Movie>() {

			@Override
			public int compare (Movie object1, Movie object2) {

				return object1.title.compareTo(object2.title);
			}
		};

		Collections.sort(list, c);
		return list.toArray(new Movie[0]);
	}

	public Movie[] selectAll () {

		List<Movie> list = new ArrayList<Movie>();
		Cursor cursor = this.db.query(TABLE_NAME,
				new String[] { "id", " score", " popularity", " rating", " originalname", " title", " imdbid", " url",
						" overview", " released", " trailer", "`cast`", "thumb" }, null, null, null, null, "title asc");
		if (cursor.moveToFirst()) {
			do {
				Movie m = new Movie();
				m.id = cursor.getInt(0);
				m.score = new Double(cursor.getString(1));
				m.popularity = new Double(cursor.getString(2));
				m.rating = new Double(cursor.getString(3));
				m.originalname = cursor.getString(4);
				m.title = cursor.getString(5);
				m.imdbid = cursor.getString(6);
				m.url = cursor.getString(7);
				m.overview = cursor.getString(8);
				m.released = cursor.getString(9);
				m.trailer = cursor.getString(10);
				// m.cast = cursor.getString(11);
				m.thumb = cursor.getString(12);
				list.add(m);
			} while (cursor.moveToNext());
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return list.toArray(new Movie[0]);
	}

	public int updateRating (int id, float rating) {

		ContentValues values = new ContentValues();
		values.put("rating", String.valueOf(rating));
		return this.db.update(TABLE_NAME, values, "id=?", new String[] { String.valueOf(id) });
	}

	public String generateUserList () {

		StringBuilder returnlist = new StringBuilder();
		int count = 0;
		Cursor cursor = this.db.query(USER_TABLE, new String[] { "id" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				if (count > 0)
					returnlist.append(", ");

				String rating = "0";
				Cursor ratingCursor = this.db.query(TABLE_NAME, new String[] { "rating" }, "id=?",
						new String[] { String.valueOf(cursor.getInt(0)) }, null, null, null);
				if (ratingCursor.moveToFirst()) {
					do {
						rating = ratingCursor.getString(0);
					} while (ratingCursor.moveToNext());
				}
				if (ratingCursor != null && !ratingCursor.isClosed()) {
					ratingCursor.close();
				}
				returnlist.append(cursor.getInt(0) + ":" + rating);
				count++;
			} while (cursor.moveToNext());
		}

		return returnlist.toString();
	}

	public int updateScores (int score, String name) {

		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("score", score);
		ContentValues updateValues = new ContentValues();
		values.put("score", score);
		
		if(getScore(name) == -1) {
			this.db.insert(SCORE_TABLE, null, values);
			return 1;
		}
	    return 0;

		
	}

	public void resetScores () {

		this.db.delete(SCORE_TABLE, null, null);
	}

	public boolean isScoreOnBoard (int score) {

		Cursor cursor = this.db.query(SCORE_TABLE, new String[] { "score" }, null, null, null, null, "score desc");
		int min = 0;
		int i = 0;
		if (cursor.moveToFirst()) {
			do {
				min = cursor.getInt(0);
				++i;
			} while (cursor.moveToNext() && i < 5);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		if (i < 5) {// The board does not have 5 scores yet, so it can be on the board
			return true;
		}
		return score > min; // Otherwise, check that it is high enough
	}

	public int getScoreCount () {

		Cursor cursor = this.db.query(SCORE_TABLE, new String[] { "count(*)" }, null, null, null, null, null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return 0;
	}

	public int getScore (String name) {

		Cursor cursor = this.db.query(SCORE_TABLE, new String[] { "score" }, "name=?", new String[] { name }, null,
				null, null);
		if (cursor.moveToFirst()) {
			return cursor.getInt(0);
		}
		return -1;
	}

	public String[] getTopNames () {

		Cursor cursor = this.db.query(SCORE_TABLE, new String[] { "name", "score" }, null, null, null, null,
				"score desc");
		String[] names = new String[5];
		int i = 0;
		if (cursor.moveToFirst()) {
			do {
				names[i] = cursor.getString(0);
				++i;
			} while (cursor.moveToNext() && i < 5);
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return names;
	}

	private static class OpenHelper extends SQLiteOpenHelper {

		OpenHelper (Context context) {

			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate (SQLiteDatabase db) {

			db.execSQL("CREATE TABLE "
					+ TABLE_NAME
					+ " (id INTEGER PRIMARY KEY, score text, popularity text, rating text, "
					+ "originalname text, title text, imdbid text, url text, overview text, released text, trailer text, `cast` text, thumb text)");
			db.execSQL("CREATE TABLE " + USER_TABLE + " (id INTEGER PRIMARY KEY)");
			db.execSQL("CREATE TABLE " + SHARED_TABLE + " (id INTEGER PRIMARY KEY, movielist TEXT)");
			db.execSQL("CREATE TABLE " + SCORE_TABLE + " (name TEXT PRIMARY KEY, score INTEGER)");
		}

		@Override
		public void onUpgrade (SQLiteDatabase db, int oldVersion, int newVersion) {

			db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
			db.execSQL("DROP TABLE IF EXISTS " + USER_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + SHARED_TABLE);
			db.execSQL("DROP TABLE IF EXISTS " + SCORE_TABLE);
			onCreate(db);
		}
	}
}
