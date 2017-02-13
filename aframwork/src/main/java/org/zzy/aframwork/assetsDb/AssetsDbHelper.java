package org.zzy.aframwork.assetsDb;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by admin on 16/11/16.
 */
public class AssetsDbHelper extends SQLiteAssetHelper {
    private static int DATABASE_VERSION = 1;
/*****************************************************************************************************/

    public AssetsDbHelper(Context context, String dbName) {
        super(context, dbName, null, DATABASE_VERSION);
    }
    public void close(){
        super.close();
    }

    public void resetDb(){
        close();
        setForcedUpgrade(DATABASE_VERSION++);
    }
}
