package com.example.suitcase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "suitcase.db";
    public static final String ITEMS_TABLE_NAME = "items";
    public static final String Col1 = "id";
    public static final String Col2 = "name";
    public static final String Col3 = "price";
    public static final String Col4 = "description";
    public static final String Col5 = "image";
    public static final String Col6 = "purchased";
    public static final int DB_VERSION=2;
    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String itemsTableQuery = "CREATE TABLE " + ITEMS_TABLE_NAME + " (" +
                Col1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Col2 + " TEXT NOT NULL, " +
                Col3 + " REAL NOT NULL, " +
                Col4 + " TEXT NOT NULL, " +
                Col5 + " TEXT NOT NULL, " +
                Col6 + " INTEGER NOT NULL)";


        try {
            db.execSQL(itemsTableQuery);

        }catch (SQLException e){
            e.printStackTrace();
        }


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + ITEMS_TABLE_NAME);
        onCreate(db);

    }


    //Insert Items Database

    public Boolean insertItems(String name,  double price, String description, boolean purchased, String image) {
        SQLiteDatabase database = getWritableDatabase();
        Log.d("DatabaseHelper", "Inserting into table: " + ITEMS_TABLE_NAME);
        Log.d("DatabaseHelper", "Inserting values: " + name + ", " + price + ", " + description + ", " + image + ", " + purchased);

        String sql = "INSERT INTO " + ITEMS_TABLE_NAME + " (name, price, description, image, purchased) VALUES (?, ?, ?, ?, ?)";
        SQLiteStatement statement = database.compileStatement(sql);
        statement.clearBindings();
        statement.bindString(1, name);
        statement.bindDouble(2, price);
        statement.bindString(3, description);
        statement.bindString(4, image);
        statement.bindLong(5, purchased ? 1 : 0);

        long result = -1;
        try {
            result = statement.executeInsert();
        } catch (SQLException e) {
            Log.e("DatabaseHelper", "Error inserting into database: ", e);
        } finally {
            database.close();
        }
        return result != -1;
    }

    //Get data from database table and column data
    public Cursor getItemById(int id){
        SQLiteDatabase database = getWritableDatabase();
        String sqlQuery = "SELECT * FROM " + ITEMS_TABLE_NAME + " WHERE "+ Col1 + "=?";
        return database.rawQuery(sqlQuery, new String[]{String.valueOf(id)});
    }

    // New method to get all data as ArrayList<ItemsModel>

    public ArrayList<ItemsModel> getAllItems() {
        ArrayList<ItemsModel> itemsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ITEMS_TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ItemsModel item = new ItemsModel();
                item.setId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(Col1))));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(Col2)));
                item.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(Col3)));
                item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Col4)));

                item.setImage(Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(Col5))));
                item.setPurchased(cursor.getInt(cursor.getColumnIndexOrThrow(Col6)) == 1);
                itemsList.add(item);
            }
            cursor.close();
        }
        db.close();
        return itemsList;
    }
    public ArrayList<ItemsModel> getPurchasedItems() {
        ArrayList<ItemsModel> itemsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ITEMS_TABLE_NAME, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                ItemsModel item = new ItemsModel();
                item.setId(String.valueOf(cursor.getInt(cursor.getColumnIndexOrThrow(Col1))));
                item.setName(cursor.getString(cursor.getColumnIndexOrThrow(Col2)));
                item.setPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(Col3)));
                item.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(Col4)));

                item.setImage(Uri.parse(cursor.getString(cursor.getColumnIndexOrThrow(Col5))));
                item.setPurchased(cursor.getInt(cursor.getColumnIndexOrThrow(Col6)) == 1);
                itemsList.add(item);
            }
            cursor.close();
        }
        db.close();
        return itemsList;
    }



    // In DatabaseHelper Java

    public ArrayList<ItemsModel> getPurchasedItems;

    // get all data from database
    public Cursor getAllData(){
        SQLiteDatabase database = getReadableDatabase();
        String sqlQuery = "SELECT * FROM " + ITEMS_TABLE_NAME;
        return database.rawQuery(sqlQuery,null);
    }
    // Edit Items Method
    public Boolean update(String id,
                          String name,
                          double price,
                          String description,
                          Uri image,
                          boolean purchased){
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Col2,name);
        cv.put(Col3,price);
        cv.put(Col4,description);
        cv.put(Col5,image.toString());
        cv.put(Col6,purchased ? 1 : 0);
        int result = database.update(ITEMS_TABLE_NAME,cv,Col1 + "=?",
                new String[]{String.valueOf(id)});
        Log.d("databaseHelper:","result" + result);
        database.close();
        return result != -1;
    }
    //Delete Data from Database
    public void deleteItem(long id){
        SQLiteDatabase database = getWritableDatabase();
        database.delete(ITEMS_TABLE_NAME,Col1 + "=?",
                new String[]{String.valueOf(id)});
        database.close();
    }

}
