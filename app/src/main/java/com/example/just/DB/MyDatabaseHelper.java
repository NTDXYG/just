package com.example.just.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.just.Bean.Story;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/5/27.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_LOVE = "create table if not exists Love(" +
            "id integer primary key autoincrement," +
            "name text," +
            "image text," +
            "url text)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_LOVE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addData(String name, String image, String url) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("image", image);
        values.put("url", url);
        db.insert("Love", null, values);
        values.clear();
    }

    public List<Story> queryData() {
        List<Story> loveList = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("Love", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                loveList.add(new Story(name, url, image, true));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return loveList;
    }

    public void deleteAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Love",null,null);
    }


    public void deleteByName(String name){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("Love","name = " +"'"+ name+"'",null);
    }
    
    public boolean isExist(String name){
        Story story = null;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query("Love", null, "name = "+ "'"+name+"'", null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String name1 = cursor.getString(cursor.getColumnIndex("name"));
                String image = cursor.getString(cursor.getColumnIndex("image"));
                String url = cursor.getString(cursor.getColumnIndex("url"));
                story = new Story(name, url, image, true);
            } while (cursor.moveToNext());
        }
        cursor.close();
        if (story != null){
            return true;
        }else {
            return false;
        }
    }
}
