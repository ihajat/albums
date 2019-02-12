package com.example.owner.myalbumsapplication.application;

import android.app.Application;

import com.example.owner.myalbumsapplication.di.AppComponent;
import com.example.owner.myalbumsapplication.di.DaggerAppComponent;
import com.example.owner.myalbumsapplication.di.module.ApiModule;
import com.example.owner.myalbumsapplication.di.module.AppModule;
import com.example.owner.myalbumsapplication.di.module.DaoModule;
import com.example.owner.myalbumsapplication.di.module.NetModule;
import com.example.owner.myalbumsapplication.di.module.RepositoryModule;


public class MyApplication extends Application {

    private static AppComponent appComponent;

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    public void onCreate(){
        super.onCreate();

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .apiModule(new ApiModule())
                .netModule(new NetModule("https://jsonplaceholder.typicode.com"))
                .daoModule(new DaoModule())
                .repositoryModule(new RepositoryModule())
                .build();

    }
}
