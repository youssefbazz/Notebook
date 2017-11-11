package com.youssefbazzaoui.notebookapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by youssefbazzaoui on 28/10/2017.
 */


public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String COL_EN = "english";
    private static final String COL_FR = "french";
    private static final String COL_DOMAIN_NAME = "name";
    private static final String TABLE_DICTIONNARY = "dic_en_fr";
    private static final String TABLE_DOMAINS = "domains";
    private static final String TABLE_DOMAIN_DICTIONNARY = "domain_dic";
    private static final String COL_DOMAIN_ID = "domain_id";


    public DatabaseHelper(Context context) {
        super(context, TABLE_DICTIONNARY, null, 3);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = " CREATE TABLE " + TABLE_DICTIONNARY + " (" + COL_EN + " varchar(100) PRIMARY KEY,  " +
                COL_FR + " TEXT)";
        db.execSQL(createTable);

        createTable = " CREATE TABLE " + TABLE_DOMAINS + " (" + COL_DOMAIN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,  " +
                COL_DOMAIN_NAME + " TEXT)";
        db.execSQL(createTable);

        createTable = " CREATE TABLE " + TABLE_DOMAIN_DICTIONNARY + " (" + COL_DOMAIN_ID + " INTEGER, " + COL_EN + " varchar(100), PRIMARY KEY (" + COL_DOMAIN_ID + "," + COL_EN + "))";
        db.execSQL(createTable);

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DICTIONNARY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOMAINS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DOMAIN_DICTIONNARY);
        onCreate(db);

    }

    public boolean addData(String eng, String fr) {
        SQLiteDatabase db = this.getWritableDatabase();
        //onUpgrade(db, 1, 2);// onCreate(db);

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EN, eng);
        contentValues.put(COL_FR, fr);
        Log.d(TAG, "addData: Adding " + eng + " to " + TABLE_DICTIONNARY);
        long result = db.insert(TABLE_DICTIONNARY, null, contentValues);
        if (result == -1) {
            return false;
        } else return true;

    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DICTIONNARY + " ORDER by " + COL_EN;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Returns only the ID that matches the name passed in
     *
     * @param name
     * @return
     */
    public Cursor getItemID(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL_EN + " FROM " + TABLE_DICTIONNARY +
                " WHERE " + COL_FR + " = '" + name + "'";
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    /**
     * Updates the Entry field
     *
     * @param oldId
     * @param newEng
     * @param newFr
     */
    public void updateEntry(String oldId, String newEng, String newFr) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_DICTIONNARY + " SET " + COL_FR +
                " = '" + newFr + "', " + COL_EN + " = '" + newEng + "' WHERE " + COL_EN + " = '" + oldId + "'";
        //" AND " + COL2 + " = '" + oldName + "'";
        Log.d(TAG, "updateName: query: " + query);
        Log.d(TAG, "updateName: Setting name to " + newEng + " /" + newFr);
        db.execSQL(query);
    }

    /**
     * Delete from database
     *
     * @param eng
     * @param fr
     */
    public void deleteName(String eng, String fr) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_DICTIONNARY + " WHERE "
                + COL_EN + " = '" + eng + "'" +
                " AND " + COL_FR + " = '" + fr + "'";
        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + eng + " from database.");
        db.execSQL(query);
    }

    public Cursor getDomains() {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_DOMAINS + " ORDER by " + COL_DOMAIN_NAME;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    public boolean addDomain(String domain) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DOMAIN_NAME, domain);
        Log.d(TAG, "addData: Adding " + domain + " to " + TABLE_DOMAINS);
        long result = db.insert(TABLE_DOMAINS, null, contentValues);
        if (result == -1) {
            return false;
        } else return true;
    }


    public void deleteDomain(String domain) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_DOMAIN_DICTIONNARY + " WHERE " + COL_DOMAIN_ID + "=" + getDomainId(domain);
        db.execSQL(query);
        query = "DELETE FROM " + TABLE_DOMAINS + " WHERE "
                + COL_DOMAIN_NAME + " = \"" + domain + "\"";

        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + COL_DOMAIN_NAME + " from database.");
        db.execSQL(query);
    }

    public int updateDomain(String newDomainName, String oldDomainName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_DOMAIN_NAME, newDomainName);
        // UPDate domain SET name="XX"  WHERE domain="OLDX"
        return db.update(TABLE_DOMAINS, contentValues, COL_DOMAIN_NAME + "=?", new String[]{oldDomainName});
    }


    public Cursor getDataByDomain(String selectedDomain) {
        String domainId = getDomainId(selectedDomain);
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL_EN + " FROM " + TABLE_DOMAIN_DICTIONNARY + " WHERE " + COL_DOMAIN_ID + "=" + domainId + " ORDER by " + COL_EN;
        Cursor data = db.rawQuery(query, null);
        return data;
    }

    private String getDomainId(String selectedDomain) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL_DOMAIN_ID + " FROM " + TABLE_DOMAINS + " where " + COL_DOMAIN_NAME + "=\"" + selectedDomain + "\" ORDER by " + COL_DOMAIN_NAME;
        Cursor data = db.rawQuery(query, null);
        data.moveToFirst();
        return data.getString(0);
    }

    public boolean addToDomain(String domain, String word) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EN, word);
        contentValues.put(COL_DOMAIN_ID, getDomainId(domain));

        Log.d(TAG, "addData: Adding to " + domain + " word " + word);
        long result = db.insert(TABLE_DOMAIN_DICTIONNARY, null, contentValues);
        if (result == -1) {
            return false;
        } else return true;
    }

    public void removeFromDomain(String domain, String word) {

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_DOMAIN_DICTIONNARY + " WHERE "
                + COL_DOMAIN_ID + " = " + getDomainId(domain) + " and " + COL_EN + "=\"" + word + "\"";

        Log.d(TAG, "deleteName: query: " + query);
        Log.d(TAG, "deleteName: Deleting " + domain + " from " + word + ".");
        db.execSQL(query);

    }
}
