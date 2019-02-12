package com.example.owner.myalbumsapplication.utils;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.example.owner.myalbumsapplication.dto.ApiResponse;

import retrofit2.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;

public class ApiUtil {
    public static <T> LiveData<ApiResponse<T>> successCall(T data) {
        return createCall(Response.success(data));
    }
    public static <T> LiveData<ApiResponse<T>> createCall(Response<T> response) {
        MutableLiveData<ApiResponse<T>> data = new MutableLiveData<>();
        data.setValue(new ApiResponse<>(response));
        return data;
    }
}