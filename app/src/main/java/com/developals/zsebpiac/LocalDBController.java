package com.developals.zsebpiac;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.developals.zsebpiac.Dao.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LocalDBController extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "local.db";
    private static final int DATABASE_VERSION = 9;

    private static LocalDBController ourInstance;
    private SQLiteDatabase database;

    public LocalDBController(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        database = this.getWritableDatabase();
    }

    public static LocalDBController getInstance(Context context){
        if(ourInstance == null)
            ourInstance = new LocalDBController(context);
        return ourInstance;
    }

    public static  LocalDBController getInstance(){
        if(ourInstance == null){
            throw new IllegalArgumentException("Parameter context missing");
        }
        return ourInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query;
        query = "CREATE TABLE IF NOT EXISTS user (" +
                    "id INT PRIMARY KEY," +
                    "email VARCHAR(45)," +
                    "first_name VARCHAR(45)," +
                    "last_name VARCHAR(45))";
                    //producer_id -> nem kell
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS phone (" +
                    "number VARCHAR(15) PRIMARY KEY," +
                    "user_id INT)";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS displayed_name (" +
                    "user_id INT PRIMARY KEY," +
                    "name VARCHAR(90))";
        db.execSQL(query);

        query = "CREATE TABLE IF NOT EXISTS location (" +
                "id INT PRIMARY KEY," +
                "zip_code CHAR(4)," +
                "city VARCHAR(60)," +
                "street_address VARCHAR(100)," +
                "user_id INT)";
        db.execSQL(query);

        query = "DELETE FROM phone WHERE number = ?";
                db.execSQL(query, new String [] {""});
        //query = "INSERT INTO user VALUES (?, ?, ?, ?)";
        //db.execSQL(query, new String [] {"0", "root", "root", "root"});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       onCreate(db);
    }

    public User getUserData(String email){
        User user = new User();
        String query;
        Cursor cursor;

        query = "SELECT * FROM user WHERE email = ?";
        cursor = database.rawQuery(query, new String[] {email});

        if(cursor.moveToFirst()){
            user.setUserId(cursor.getString(cursor.getColumnIndex("id")));
            user.setEmail(cursor.getString(cursor.getColumnIndex("email")));
            user.setFirstName(cursor.getString(cursor.getColumnIndex("first_name")));
            user.setLastName(cursor.getString(cursor.getColumnIndex("last_name")));
            user.getPhones().addAll(getPhoneNumbers(user.getUserId()));
            user.setDisplayedName(getDisplayedName(user.getUserId()));
            user.getLocations().addAll(getLocations(user.getUserId()));
        }
        return user;
    }

    private List<String> getPhoneNumbers(String id){
        List<String> list = new ArrayList<>();
        String query;

        query = "SELECT number FROM phone WHERE user_id = ?";
        Cursor cursor = database.rawQuery(query, new String[] {id});
        if(cursor.moveToFirst()){
            do{
                list.add(cursor.getString(0));
            }while(cursor.moveToNext());
        }
        return list;
    }

    private String getDisplayedName(String id){
        String name = "";
        String query;

        query = "SELECT name FROM displayed_name WHERE user_id = ?";
        Cursor cursor = database.rawQuery(query, new String [] {id});
        if(cursor.moveToFirst()){
            name = cursor.getString(0);
        }
        return name;
    }

    private List<String []> getLocations(String id){
        List<String []> locations = new ArrayList<>();
        String query;

        query = "SELECT zip_code,city,street_address FROM location WHERE user_id = ?";
        Cursor cursor = database.rawQuery(query, new String[] {id});
        if(cursor.moveToFirst()){
            do{
                String [] item = new String[3];
                item[0] = cursor.getString(0);
                item[1] = cursor.getString(1);
                item[2] = cursor.getString(2);
                locations.add(item);
            }while(cursor.moveToNext());
        }
        return locations;
    }


}
