package com.dmart.objectnosql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String tblObject = "Object";
    public static final String colObjectUuid = "ObjectUuid";
    public static final String colType = "Type";
    public static final String colKeywords = "Keywords";
    public static final String colContent = "Content";

    private static final String dbName = "objectDB";
    private static final int    dbVersion = 1;

    public SQLiteHelper(Context context) {
        super(context, dbName, null, dbVersion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "CREATE TABLE IF NOT EXISTS " + tblObject + " (" +
                        colObjectUuid + " VARCHAR(36) NOT NULL, " +
                        colType + " VARCHAR(512) NOT NULL, " +
                        colKeywords + " TEXT NOT NULL, " +
                        colContent + " TEXT NOT NULL, " +
                        "PRIMARY KEY (" + colObjectUuid + ", " + colType + ") ON CONFLICT REPLACE);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + tblObject);
        onCreate(db);
    }
}