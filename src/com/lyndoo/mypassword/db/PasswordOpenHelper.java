package com.lyndoo.mypassword.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class PasswordOpenHelper extends SQLiteOpenHelper {

	public static final String CREATE_RECORD = "create table record("
				+"id integer primary key autoincrement, "
				+"title varchar(30), "
				+ "category integer, "
				+"firstLetter char(1))";
	
	public static final String CREATE_ITEM = "create table item("
			+"id integer primary key autoincrement, "
			+"recordID integer, "
			+"title varchar(10), "
			+"content text, "
			+ "type integer, "
			+ "order integer)";
	
	public PasswordOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_RECORD);
		db.execSQL(CREATE_ITEM);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
