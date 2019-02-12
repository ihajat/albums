package com.example.owner.myalbumsapplication;
import android.support.test.espresso.IdlingResource;

import com.example.owner.myalbumsapplication.ui.MainActivity;


public class MainActivityIdlingResource implements IdlingResource {

    private MainActivity activity;
    private ResourceCallback callback;

    public MainActivityIdlingResource(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    public String getName() {
        return "MainActivityIdleName";
    }

    @Override
    public boolean isIdleNow() {
        Boolean idle = isIdle();
        if (idle) callback.onTransitionToIdle();
        return idle;
    }

    public boolean isIdle() {
        return activity != null && callback != null ;//&& activity.isSyncFinished()
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}