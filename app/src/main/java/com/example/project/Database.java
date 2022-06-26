package com.example.project;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {
    private static final String databasename="Address_database.db";
    private static final int versiyon=1;
    private static final String ID="_id";
    private static final String NAME="name";
    private static final String ADDRESS="address";
    private static final String ADRESS_TYPE="address_type";
    private static final String LATITUDE="latitude";
    private static final String LONGITUDE="longitude";
    private static final String KLASSEN_CREATE = "CREATE TABLE Stundenplan(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "name TEXT NOT NULL);";
    private static final String hadi_bakiyim = "CREATE TABLE DogancanTablo(" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT, "+
            "name TEXT NOT NULL, "+
            "address TEXT NOT NULL, "+
            "address_type TEXT NOT NULL, "+
            "latitude TEXT NOT NULL, "+
            "longitude TEXT NOT NULL);";
    public Database(Context context) {

        super(context,databasename,null,versiyon);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(hadi_bakiyim);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+"DogancanTablo");
        onCreate(sqLiteDatabase);

    }
    public boolean insert_data(String name,String address,String address_type,String latitude,String longitude){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(NAME,name);
        contentValues.put(ADDRESS,address);
        contentValues.put(ADRESS_TYPE,address_type);
        contentValues.put(LATITUDE,latitude);
        contentValues.put(LONGITUDE,longitude);
        long result=sqLiteDatabase.insert("DogancanTablo",null,contentValues);
        if(result==-1){
            return false;
        }
        else{
            return true;
        }
    }
    public Cursor getAllData(){
        SQLiteDatabase sqLiteDatabase=this.getReadableDatabase();
        Cursor cs=sqLiteDatabase.rawQuery("select * from "+"DogancanTablo",null);
        return cs;
    }
    public boolean update_data(String id,String name,String address,String address_type){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(ID,id);
        contentValues.put(NAME,name);
        contentValues.put(ADDRESS,address);
        contentValues.put(ADRESS_TYPE,address_type);
        sqLiteDatabase.update("DogancanTablo",contentValues, "_id = ?",new String[]{id});
        return true;

    }

}
