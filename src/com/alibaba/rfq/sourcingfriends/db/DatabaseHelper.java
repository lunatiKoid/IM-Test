package com.alibaba.rfq.sourcingfriends.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	public final static String NAME = "SF";
	public final static int VERSION = 1;

	public DatabaseHelper(Context context) {
		super(context, NAME, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("CREATE TABLE USER_PROFILE_RFQ (user_id integer primary key autoincrement, name varchar(20), passwd varchar(20), company varchar(100),photo blob)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS USER_PROFILE_RFQ");
		onCreate(db);
	}

}