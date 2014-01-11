package com.alibaba.rfq.sourcingfriends.db;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.rfq.sourcingfriends.dto.UserProfileDTO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class DatabaseService {

    private DatabaseHelper dbHelper;

    public DatabaseService(Context context) {
        this.dbHelper = new DatabaseHelper(context);
    }

    // // �������ݡ�
    // public void insertUserDO(UserProfileDTO person) {
    // SQLiteDatabase db = dbHelper.getWritableDatabase();
    //
    // ContentValues values = new ContentValues();
    //
    // final ByteArrayOutputStream os = new ByteArrayOutputStream();
    // Bitmap bmp = person.getPhoto();
    // bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
    // values.put("photo", os.toByteArray());
    // /**
    // * Bitmap.CompressFormat.JPEG �� Bitmap.CompressFormat.PNG JPEG �� PNG ������������ JPEG����������ͼ��PNGʹ�ô�LZ77��������������ѹ���㷨��
    // * ���ｨ��ʹ��PNG��ʽ���� 100 ��ʾ��������Ϊ100%����Ȼ��Ҳ���Ըı��������Ҫ�İٷֱ������� os �Ƕ�����ֽ������ .compress() �����ǽ�Bitmapѹ����ָ����ʽ�������������
    // */
    // values.put("name", person.getUserName());
    // values.put("passwd", person.getPasswd());
    // values.put("company", person.getCompanyName());
    // db.insert("USER_PROFILE_RFQ", null, values);
    // }

    // public void savePersons(List<UserProfileDTO> persons) {
    //
    // for (Person person : persons) {
    // SQLiteDatabase db = dbHelper.getWritableDatabase();
    // ContentValues values = new ContentValues();
    // final ByteArrayOutputStream os = new ByteArrayOutputStream();
    // Bitmap bmp = person.getPhoto();
    // bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
    // values.put("photo", os.toByteArray());
    // /**
    // * Bitmap.CompressFormat.JPEG �� Bitmap.CompressFormat.PNG JPEG �� PNG
    // * ������������ JPEG����������ͼ��PNGʹ�ô�LZ77��������������ѹ���㷨�� ���ｨ��ʹ��PNG��ʽ���� 100
    // * ��ʾ��������Ϊ100%����Ȼ��Ҳ���Ըı��������Ҫ�İٷֱ������� os �Ƕ�����ֽ������
    // *
    // * .compress() �����ǽ�Bitmapѹ����ָ����ʽ�������������
    // */
    // values.put("name", person.getName());
    // values.put("age", person.getAge());
    // db.insert("person", null, values);
    // }
    //
    // }

    // �������� public
    public int update(String tableName, ContentValues values, String whereClause, String[] whereArgs) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        return db.update(tableName, values, whereClause, whereArgs);
        // db.execSQL("update USER_PROFILE_RFQ set name=? ,passwd=?, company=?, photo=? where user_id=?", new Object[] {
        // person.getUserName(), person.getPasswd(), person.getCompanyName(), person.getPhoto(), person.getId() });
    }

    // �������ݡ�
    public void insertByDO(String tableName, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // final ByteArrayOutputStream os = new ByteArrayOutputStream();
        // Bitmap bmp = person.getPhoto();
        // bmp.compress(Bitmap.CompressFormat.PNG, 100, os);
        // values.put("photo", os.toByteArray());
        //
        // //Bitmap.CompressFormat.JPEG �� Bitmap.CompressFormat.PNG JPEG �� PNG ������������ JPEG����������ͼ��PNGʹ�ô�LZ77��������������ѹ���㷨��
        // //���ｨ��ʹ��PNG��ʽ���� 100 ��ʾ��������Ϊ100%����Ȼ��Ҳ���Ըı��������Ҫ�İٷֱ������� os �Ƕ�����ֽ������ .compress() �����ǽ�Bitmapѹ����ָ����ʽ�������������
        //
        // values.put("name", person.getUserName());
        // values.put("passwd", person.getPasswd());
        // values.put("company", person.getCompanyName());

        db.insert(tableName, null, values);
    }

    // select * from tableName where map<>
    public Cursor select2DO(String tableName, String[] coloums, String[] skeys, String[] svalues) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        String sql = " SELECT ";
        if (coloums != null && coloums.length > 0) {
            sql += coloums[0];
            for (int i = 1; i < coloums.length; i++) {
                sql += ",";
                sql += coloums[i];
            }
        }
        sql += " FROM " + tableName + " ";

        if (skeys != null && svalues != null && 0 < skeys.length && skeys.length == svalues.length) {
            sql += " WHERE ";
            sql += (skeys[0] + "=? ");
            for (int i = 1; i < skeys.length; i++) {
                sql += " AND";
                sql += (skeys[0] + "=? ");
            }
            Log.i("DatabaseService", sql);
            cursor = db.rawQuery(sql, svalues);

        } else {
            cursor = db.rawQuery(sql, null);
        }
        return cursor;
    }

    // // ��������
    // public UserProfileDTO findById(Integer id) {
    // SQLiteDatabase db = dbHelper.getReadableDatabase();
    // Cursor cursor = db.rawQuery("select * from USER_PROFILE_RFQ where user_id=?", new String[] { id.toString() });
    // while (cursor.moveToNext()) {
    // String name = cursor.getString(cursor.getColumnIndex("name"));
    // String passwd = cursor.getString(cursor.getColumnIndex("passwd"));
    // String company = cursor.getString(cursor.getColumnIndex("company"));
    // Integer userId = cursor.getInt(cursor.getColumnIndex("user_id"));
    // // �����ݿ��ȡͼƬ
    // byte[] in = cursor.getBlob(cursor.getColumnIndex("photo"));
    // Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
    //
    // return new UserProfileDTO(userId, name, passwd, company, bmpout);
    // }
    // return null;
    // }

    /*
     * // ��������,ͨ���û����������� public UserProfileDTO findByNamePasswd(String name, String code) { SQLiteDatabase db =
     * dbHelper.getReadableDatabase(); Cursor cursor =
     * db.rawQuery("select * from USER_PROFILE_RFQ where name=? and passwd=?", new String[] { name, code }); while
     * (cursor.moveToNext()) { String username = cursor.getString(cursor.getColumnIndex("name")); String passwd =
     * cursor.getString(cursor.getColumnIndex("passwd")); String company =
     * cursor.getString(cursor.getColumnIndex("company")); Integer userId =
     * cursor.getInt(cursor.getColumnIndex("user_id")); // �����ݿ��ȡͼƬ byte[] in =
     * cursor.getBlob(cursor.getColumnIndex("photo")); Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
     * return new UserProfileDTO(userId, username, passwd, company, bmpout); } return null; }
     */

    /*
     * // ɾ������ public void deleteById(Integer id) { SQLiteDatabase db = dbHelper.getReadableDatabase();
     * db.execSQL("delete from USER_PROFILE_RFQ where user_id=?", new Object[] { id }); }
     */

    /*
     * public Long getUsersCount() { SQLiteDatabase db = dbHelper.getReadableDatabase(); Cursor cursor =
     * db.rawQuery("select count(*) from USER_PROFILE_RFQ", null); cursor.moveToFirst(); return cursor.getLong(0); }
     */

    // public List<UserProfileDTO> getScrollData(int offer, int maxResult) {
    // List<UserProfileDTO> persons = new ArrayList<UserProfileDTO>();
    // SQLiteDatabase db = dbHelper.getReadableDatabase();
    // Cursor cursor = db
    // .rawQuery("select * from person limit ?,?", new String[] {
    // String.valueOf(offer), String.valueOf(maxResult) });
    // while (cursor.moveToNext()) {
    //
    // String name = cursor.getString(cursor.getColumnIndex("name"));
    // String company = cursor.getString(cursor.getColumnIndex("company"));
    // Integer userId = cursor.getInt(cursor.getColumnIndex("user_id"));
    // // �����ݿ��ȡͼƬ
    // byte[] in = cursor.getBlob(cursor.getColumnIndex("photo"));
    // Bitmap bmpout = BitmapFactory.decodeByteArray(in, 0, in.length);
    //
    // UserProfileDTO person = new UserProfileDTO(userId,name,company,bmpout);
    // persons.add(person);
    //
    // }
    // return persons;
    // }
}
