package org.zzy.aframwork.database;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by admin on 16/11/16.
 */
public class MyDatabase extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "northwind.db";
    private static final int DATABASE_VERSION = 1;

    public MyDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
}