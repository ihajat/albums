package com.example.owner.myalbumsapplication.utils;


import android.arch.core.executor.testing.InstantTaskExecutorRule;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.owner.myalbumsapplication.dto.ApiResponse;
import com.example.owner.myalbumsapplication.network.NetworkBoundResource;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

@RunWith(Parameterized.class)
public class NetworkBoundResourceTest {
    @Rule
    public InstantTaskExecutorRule instantExecutorRule = new InstantTaskExecutorRule();

    private Function<Foo, Void> saveCallResult;

    private Function<Foo, Boolean> shouldFetch;

    private Function<Void, LiveData<ApiResponse<Foo>>> createCall;

    private MutableLiveData<Foo> dbData = new MutableLiveData<>();

    private NetworkBoundResource<Foo, Foo> networkBoundResource;

    private AtomicBoolean fetchedOnce = new AtomicBoolean(false);
    private CountingAppExecutors countingAppExecutors;
    private final boolean useRealExecutors;

    @Parameterized.Parameters
    public static List<Boolean> param() {
        return Arrays.asList(true, false);
    }

    public NetworkBoundResourceTest(boolean useRealExecutors) {
        this.useRealExecutors = useRealExecutors;
        if (useRealExecutors) {
            countingAppExecutors = new CountingAppExecutors();
        }
    }

    @Before
    public void init() {
        AppExecutors appExecutors = useRealExecutors
                ? countingAppExecutors.getAppExecutors()
                : new InstantAppExecutors();
        networkBoundResource = new NetworkBoundResource<Foo, Foo>() { //auditor
            @Override
            protected void saveCallResult(@NonNull Foo item) {
                saveCallResult.apply(item);
            }

            @Override
            protected boolean shouldFetch(@Nullable Foo data) {
                // since test methods don't handle repetitive fetching, call it only once
                return shouldFetch.apply(data) && fetchedOnce.compareAndSet(false, true);
            }

            @NonNull
            @Override
            protected LiveData<Foo> loadFromDb() {
                return dbData;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<Foo>> createCall() {
                return createCall.apply(null);
            }
        };
    }

    private void drain() {
        if (!useRealExecutors) {
            return;
        }
        try {
            countingAppExecutors.drainTasks(1, TimeUnit.SECONDS);
        } catch (Throwable t) {
            throw new AssertionError(t);
        }
    }

    @Test
    public void basicFromNetwork() {
        //TODO
    }

    @Test
    public void failureFromNetwork() {
        //TODO
    }

    @Test
    public void dbSuccessWithoutNetwork() {
        //TODO
    }

    @Test
    public void dbSuccessWithFetchFailure() {
        //TODO
    }

    @Test
    public void dbSuccessWithReFetchSuccess() {
        //TODO
    }

    static class Foo {

        int value;

        Foo(int value) {
            this.value = value;
        }
    }
}