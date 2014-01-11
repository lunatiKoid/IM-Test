package com.alibaba.rfq.sourcingfriends.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public final static String NAME    = "SF";
    public final static int    VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        // db.execSQL("CREATE TABLE IF NOT EXISTS USER_PROFILE_RFQ (user_id integer primary key autoincrement, name varchar(20), passwd varchar(20), company varchar(100),photo blob)");
        db.execSQL("CREATE TABLE IF NOT EXISTS USER_LOGINED_SF (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR(20), PASSWD VARCHAR(20), ISLOGIN BOOLEAN);");

        db.execSQL("CREATE TABLE IF NOT EXISTS USER_PROFILE_SF (" + "USER_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                   + "NAME VARCHAR(20)," + "PHOTO BLOB,BIZ_TYPE VARCHAR(20)," + "COMPANY VARCHAR(100),"
                   + "ADDRESS VARCHAR(50)," + "MINI_SITE VARCHAR(50)," + "ADDED DATETIME,"
                   + "MAIN_PRODUCTS VARCHAR(50)," + "RECENTLY_QUOTED_B_REQUEST VARCHAR(50));");
        db.execSQL("CREATE TABLE IF NOT EXISTS TRADE_MSG_SF(" + "TRADE_MSG_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + " PHOTO BLOB, " + "MSG_SENDER_ID INTEGER," + " MSG_SENDER_NAME VARCHAR(20),"
                   + "LASTED_MSG_CONTENT VARCHAR(333)," + "MSG_UNREAD_NUM INTEGER ,"
                   + "LASTED_MSG_RECEIVED_TIME DATETIME);");
        db.execSQL("CREATE TABLE IF NOT EXISTS ING_MSG_SF(" + "ING_MSG_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "MSG_SENDER_ID INTEGER," + " MSG_SENDER_NAME VARCHAR(20)," + "MSG_SENDED_TIME DATETIME,"
                   + "MSG_CONTENT VARCHAR(333)," + "MSG_RECEIVED_TIME DATETIME);");
        db.execSQL("CREATE TABLE IF NOT EXISTS SUPPLIER_LIST_SF(" + "SUPPLIER_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                   + "NAME VARCHAR(20)," + "PHOTO BLOB," + "BIZ_TYPE VARCHAR(20)," + "COMPANY VARCHAR(100),"
                   + "ADDRESS VARCHAR(50)," + "MINI_SITE VARCHAR(50)," + "ADDED VARCHAR(10),"
                   + "MAIN_PRODUCTS VARCHAR(50)," + "RECENTLY_QUOTED_B_REQUEST VARCHAR(50)" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        // TODO Auto-generated method stub
        // db.execSQL("DROP TABLE IF EXISTS USER_PROFILE_RFQ");
        db.execSQL("DROP TABLE IF EXISTS USER_PROFILE_SF");
        db.execSQL("DROP TABLE IF EXISTS TRADE_MSG_SF");
        db.execSQL("DROP TABLE IF EXISTS ING_MSG_SF");
        db.execSQL("DROP TABLE IF EXISTS SUPPLIER_LIST_SF");
        onCreate(db);
    }

}
