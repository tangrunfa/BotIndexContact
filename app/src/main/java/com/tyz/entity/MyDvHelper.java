package com.tyz.entity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.tyz.search.ContactDao;
import com.tyz.search.DaoMaster;

import org.greenrobot.greendao.database.Database;

public class MyDvHelper  extends DaoMaster.OpenHelper{
    public MyDvHelper(Context context, String name) {
        super(context, name);
    }

    public MyDvHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }
            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, ContactDao.class);
    }
}
