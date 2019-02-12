package com.example.owner.myalbumsapplication.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import com.example.owner.myalbumsapplication.dto.AlbumDto

@Dao
interface AlbumDao : BaseDao<AlbumDto> {

    @Query("SELECT * FROM AlbumDto  ORDER BY title LIMIT :limit OFFSET :offset")
    operator fun get(offset: Int, limit: Int): LiveData<List<AlbumDto>>

    @Query("DELETE FROM AlbumDto")
    fun deleteAll()
}

