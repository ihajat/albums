package com.example.owner.myalbumsapplication.di.module;


import com.example.owner.myalbumsapplication.api.AlbumApi;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

@Module
public class ApiModule {
    @Provides
    @Singleton
    public AlbumApi providesCatalogApi(Retrofit retrofit) {
        return retrofit.create(AlbumApi.class);
    }
}
