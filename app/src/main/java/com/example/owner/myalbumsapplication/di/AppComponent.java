package com.example.owner.myalbumsapplication.di;

import android.content.Context;

import com.example.owner.myalbumsapplication.di.module.ApiModule;
import com.example.owner.myalbumsapplication.di.module.AppModule;
import com.example.owner.myalbumsapplication.di.module.DaoModule;
import com.example.owner.myalbumsapplication.di.module.NetModule;
import com.example.owner.myalbumsapplication.di.module.RepositoryModule;
import com.example.owner.myalbumsapplication.viewmodel.MainActivityViewModel;

import javax.inject.Singleton;

import dagger.Component;


@Singleton
@Component(
        modules = {AppModule.class, NetModule.class, RepositoryModule.class, ApiModule.class, DaoModule.class}
)
public interface AppComponent {

    public void inject(MainActivityViewModel viewModelModule);

    public void inject(Context content);


}
