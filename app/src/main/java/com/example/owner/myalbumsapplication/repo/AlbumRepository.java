package com.example.owner.myalbumsapplication.repo;

import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.owner.myalbumsapplication.api.AlbumApi;
import com.example.owner.myalbumsapplication.dao.AlbumDao;
import com.example.owner.myalbumsapplication.dto.AlbumDto;
import com.example.owner.myalbumsapplication.dto.ApiResponse;
import com.example.owner.myalbumsapplication.dto.Resource;
import com.example.owner.myalbumsapplication.network.NetworkBoundResource;

import java.util.List;
import java.util.concurrent.Executor;

import javax.inject.Inject;

/**
 * This the Single source of truth!
 * This one will use the NetworkBoundResource
 */
public class
AlbumRepository {
    private static final String TAG = "AlbumRepository";
    final private AlbumApi api;
    final private AlbumDao dao;
    final private Executor executor;

    @Inject
    public AlbumRepository(AlbumApi api, AlbumDao dao, Executor executor) {
        this.api = api;
        this.dao = dao;
        this.executor = executor;

    }

    public LiveData<Resource<List<AlbumDto>>> browseRepo(final int page, final int limit) {
        final int offset = (page - 1) * limit;
        LiveData<Resource<List<AlbumDto>>> liveData = new NetworkBoundResource<List<AlbumDto>, List<AlbumDto>>() {
            @Override
            protected void saveCallResult(@NonNull List<AlbumDto> albumResponse) {
                AlbumDto[] arr = new AlbumDto[albumResponse.size()];
                albumResponse.toArray(arr);
                dao.insertAll(arr);

            }

            @Override
            protected boolean shouldFetch(@Nullable List<AlbumDto> data) {
                return data == null || data.isEmpty();//let's always refresh to be up to date. data == null || data.isEmpty();
            }

            @NonNull
            @Override
            protected LiveData<List<AlbumDto>> loadFromDb() {
                return dao.get(offset, limit);
            }


            @NonNull
            @Override
            protected LiveData<ApiResponse<List<AlbumDto>>> createCall() {
                LiveData<ApiResponse<List<AlbumDto>>> response = api.browseRepoLiveData(page, limit);
                return response;
            }
        }.getAsLiveData();

        return liveData;
    }

    public void clearCache() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                dao.deleteAll();
            }
        });
    }


}
