package com.example.owner.myalbumsapplication.api;

import android.arch.lifecycle.LiveData;

import com.example.owner.myalbumsapplication.dto.AlbumDto;
import com.example.owner.myalbumsapplication.dto.ApiResponse;
import java.util.List;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AlbumApi {

//    // fetch the list of album available
    @GET("/albums")
    LiveData<ApiResponse<List<AlbumDto>>> browseRepoLiveData(@Query("page") int page, @Query("per_page") int limit);

}
