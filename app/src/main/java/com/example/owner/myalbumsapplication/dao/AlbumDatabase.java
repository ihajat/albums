package com.example.owner.myalbumsapplication.dao;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import com.example.owner.myalbumsapplication.dto.AlbumDto;

@Database(entities = {AlbumDto.class}, version = 4, exportSchema = false)
public abstract class AlbumDatabase extends RoomDatabase {
    public abstract AlbumDao albumDao();
}

