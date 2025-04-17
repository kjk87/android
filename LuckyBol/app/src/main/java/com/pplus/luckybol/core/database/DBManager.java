package com.pplus.luckybol.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pplus.luckybol.core.database.entity.DaoMaster;
import com.pplus.luckybol.core.database.entity.DaoSession;

/**
 * Created by 김종경 on 2016-10-10.
 */

public class DBManager {

    private DaoMaster.DevOpenHelper openHelper;
    private static DBManager dbManager;

    public static DBManager getInstance(Context context){

        if(dbManager == null) {
            dbManager = new DBManager(context);
        }

        return dbManager;
    }

    private DBManager(Context context){

        if(openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, "luckybol", null);
        }

        if(daoSession == null) {
            SQLiteDatabase db = openHelper.getWritableDatabase();
            daoSession = new DaoMaster(db).newSession();
        }
    }

    public DaoSession getSession(){


        return daoSession;
    }

    DaoSession daoSession = null;
}
