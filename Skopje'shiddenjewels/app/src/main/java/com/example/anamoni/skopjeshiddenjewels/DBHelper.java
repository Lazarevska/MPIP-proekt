package com.example.anamoni.skopjeshiddenjewels;

/**
 * Created by AnaMoni on 11-Jun-17.
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDB.db";
    public static final String LOCATIONS_TABLE_NAME = "locations";
    public static final String LOCATIONS_COLUMN_ID = "id";
    public static final String LOCATIONS_COLUMN_NAME = "name";
    public static final String LOCATIONS_COLUMN_LAT = "lat";
    public static final String LOCATIONS_COLUMN_LON = "lon";

    private HashMap hp;



    public DBHelper(Context context) {
        super(context, DATABASE_NAME , null, 1);

    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS locations " +
                        "(id integer primary key, name text, lat double, lon double)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }

    public boolean insertLocation (String name, double lat, double lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("lat", lat);
        contentValues.put("lon", lon);
        db.insert("locations", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from locations where id="+id+"", null );
        return res;
    }

    public int numberOfRows(){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, LOCATIONS_TABLE_NAME);
        return numRows;
    }

    public boolean updateContact (Integer id, String name, double lat, double lon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("lat", lat);
        contentValues.put("lon", lon);
        db.update("locations", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
        return true;
    }

    public Integer delete (String[] a) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("locations", "name = 'home' ",   a );
    }

    public ArrayList<String> getAllLocations() {
        ArrayList<String> array_list = new ArrayList<String>();

        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
            Cursor res =  db.rawQuery( "select * from locations", null );
            res.moveToFirst();

            while(res.isAfterLast() == false){
                array_list.add(res.getString(res.getColumnIndex(LOCATIONS_COLUMN_NAME)));
                res.moveToNext();
            }
            res.close();
            return array_list;
    }

    public ArrayList<String> getLocation(String lo) {
        String selectQuery = "SELECT name, lat, lon FROM locations WHERE TRIM(name) = '" + lo.trim() + "'";
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor cursor      = db.rawQuery(selectQuery, null);
        ArrayList<String> data      = new ArrayList<>();
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
                // get the data into array, or class variable
                data.add(cursor.getString(cursor.getColumnIndex(LOCATIONS_COLUMN_LAT)));
                data.add(cursor.getString(cursor.getColumnIndex(LOCATIONS_COLUMN_LON)));
            cursor.moveToNext();
            }
        cursor.close();

        return data;
    }

}
