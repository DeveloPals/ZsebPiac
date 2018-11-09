package com.developals.zsebpiac.Dao;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.developals.zsebpiac.LocalDBController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class User {

    private static User current;
    private static SQLiteDatabase database;
    private String userId;
    private String email;
    private String displayedName;
    private String firstName;
    private String lastName;
    private List<String> phones;
    private List<String []> locations;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<String []> getLocations(){
        return  locations;
    }

    public String getLocation(int position){
        String [] loc = locations.get(position);
        return loc[0] + " " + loc[1] + ", " + loc[2];
    }

    public String getDisplayedName() {
        if(!displayedName.equals("")) {
            return displayedName;
        } else
            return getWholeName();
    }

    public void setDisplayedName(String displayedName) {
        String dname = displayedName.trim();
        if(!dname.equals(getWholeName())) {
            this.displayedName = dname;
        } else {
            this.displayedName = "";
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName.trim();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName.trim();
    }

    public String getWholeName(){
        return lastName + " " + firstName;
    }

    public List<String> getPhones() {
        return phones;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public User() {
        this.phones = new ArrayList<>();
        this.locations = new ArrayList<>();
        database = LocalDBController.getInstance().getWritableDatabase();
    }

    public static User getCurrent() {
        if (current == null) {
            SharedPreferences pref = getApplicationContext().getSharedPreferences("loginData", MODE_PRIVATE);
            String email = pref.getString("email", null);
            if (email != null) {
                current = LocalDBController.getInstance(getApplicationContext()).getUserData(email);
            }
        }
        return current;
    }

    public void updateName() {
        String query;
        query = "UPDATE user SET first_name = ?, last_name = ? WHERE id = ?";
        database.execSQL(query, new String[]{firstName, lastName, userId});

        query = "SELECT name FROM displayed_name WHERE user_id = ?";
        Cursor cursor = database.rawQuery(query, new String[]{userId});
        if(cursor.moveToFirst()){
            if(!displayedName.equals("")) {
                query = "UPDATE displayed_name SET name = ? WHERE user_id = ?";
                database.execSQL(query, new String[]{displayedName, userId});
            } else {
                query = "DELETE FROM displayed_name WHERE user_id = ?";
                database.execSQL(query, new String[]{userId});
            }
        } else if(!displayedName.equals("")){
            query = "INSERT INTO displayed_name (user_id, name) VALUES (?, ?)";
            database.execSQL(query, new String [] {userId, displayedName});
        }
    }


    public void addPhone(String number){
        phones.add(number);
        String query = "INSERT INTO phone (number, user_id) VALUES (?, ?)";
        database.execSQL(query, new String [] {number, userId});
    }

    public void removePhone(int position){
        String number = phones.remove(position);
        String query = "DELETE FROM phone WHERE number = ?";
        database.execSQL(query, new String [] {number});
    }

    public void addLocation(String [] loc){
        locations.add(loc);
        String query = "INSERT INTO location (zip_code, city, street_address, user_id) " +
                "VALUES (?, ?, ?, ?)";
        database.execSQL(query, new String [] {loc[0], loc[1], loc[2], userId});
    }

    public void removeLocation(int position) {
        String[] loc = locations.remove(position);
        String query = "DELETE FROM location WHERE zip_code = ? AND city = ? AND street_address = ?";
        database.execSQL(query, new String[]{loc[0], loc[1], loc[2]});
    }
}