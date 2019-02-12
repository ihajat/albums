package com.example.owner.myalbumsapplication.viewmodel;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.FragmentActivity;

import com.example.owner.myalbumsapplication.dto.AlbumDto;
import com.example.owner.myalbumsapplication.dto.Resource;
import com.example.owner.myalbumsapplication.repo.AlbumRepository;
import java.util.List;
import javax.inject.Inject;

public class MainActivityViewModel extends ViewModel {
    private AlbumRepository repository;

    final private MutableLiveData<Request> request = new MutableLiveData();
    final private LiveData<Resource<List<AlbumDto>>> result = Transformations.switchMap(request, new Function<Request, LiveData<Resource<List<AlbumDto>>>>() {
        @Override
        public LiveData<Resource<List<AlbumDto>>> apply(final Request input) {
            LiveData<Resource<List<AlbumDto>>> resourceLiveData = repository.browseRepo(input.page, input.limit);
            final MediatorLiveData<Resource<List<AlbumDto>>> mediator = new MediatorLiveData<>();
            mediator.addSource(resourceLiveData, AlbumDtos -> {
                List<AlbumDto> resp = AlbumDtos.getData();
                Resource<List<AlbumDto>> response = null;
                switch (AlbumDtos.getStatus()) {
                    case LOADING:
                        response = Resource.loading(resp);
                        break;
                    case SUCCESS:
                        response = Resource.success(resp);
                        break;
                    case ERROR:
                        response = Resource.error(AlbumDtos.getException(), null);
                        break;

                }
                mediator.setValue(response);
            });
            return mediator;
        }
    });

    public static MainActivityViewModel create(FragmentActivity activity) {
        MainActivityViewModel viewModel = ViewModelProviders.of(activity).get(MainActivityViewModel.class);
        return viewModel;
    }

    public void load(int page, int limit) {
        request.setValue(new Request(page, limit));
    }

    @Inject
    public void setRepository(AlbumRepository repository) {
        this.repository = repository;
    }

    public LiveData<Resource<List<AlbumDto>>> getResult() {
        return result;
    }

    public void clearCache() {
        repository.clearCache();
    }

    public static class Request {
        final private int page, limit;

        public Request(int page, int limit) {
            this.page = page;
            this.limit = limit;
        }

        public int getLimit() {
            return limit;
        }
    }

}
