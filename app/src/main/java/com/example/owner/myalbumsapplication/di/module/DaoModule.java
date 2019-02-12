package com.example.owner.myalbumsapplication.di.module;

import android.app.Application;
import android.arch.persistence.room.Room;
import com.example.owner.myalbumsapplication.dao.AlbumDao;
import com.example.owner.myalbumsapplication.dao.AlbumDatabase;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @Provides
    @Singleton
    public AlbumDatabase provideAlbumDatabase(Application app) {
        return Room.databaseBuilder(app,
                AlbumDatabase.class, "albums-db").build();
    }

    @Provides
    @Singleton
    public AlbumDao provideAlbumDao(AlbumDatabase db) {
        return db.albumDao();
    }
}
