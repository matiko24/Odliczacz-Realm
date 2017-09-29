package com.matekome.odliczacz;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().name("odliczacz.realm").build();
        //Realm.deleteRealm(realmConfig);
        //Realm.init(this);
        Realm.setDefaultConfiguration(realmConfig);
    }
}
