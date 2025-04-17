package com.pplus.prnumberbiz.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.pplus.prnumberbiz.core.database.dao.DaoMaster;
import com.pplus.prnumberbiz.core.database.dao.DaoSession;

/**
 * Created by 김종경 on 2016-10-10.
 */

public class DBManager{

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
            openHelper = new DaoMaster.DevOpenHelper(context, "PPlusDb", null);
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
