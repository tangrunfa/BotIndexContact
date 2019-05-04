package com.tyz.application;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import com.tyz.entity.DaoManager;

public class contactApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initgreenDaoDB();
    }

    private void initgreenDaoDB(){
        DaoManager.getInstance().init(this);

    }

}
