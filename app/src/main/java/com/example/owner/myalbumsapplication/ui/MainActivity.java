package com.example.owner.myalbumsapplication.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;
import com.example.owner.myalbumsapplication.R;
import com.example.owner.myalbumsapplication.application.MyApplication;
import com.example.owner.myalbumsapplication.guiView.MainView;
import com.example.owner.myalbumsapplication.viewmodel.MainActivityViewModel;

public class MainActivity extends BaseActivity {
    public static final int LIMIT = 100;
    private int page = 1;
    private MainView view;
    private MainActivityViewModel modelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        //All view related things are in MainView, this pattern makes it easy to apply a/b testing or changing the design easily
        view = new MainView(getLayoutInflater(), null, new MainView.Listener() {
            @Override
            public void loadMore() {
                loadMoreObjects();
            }

            @Override
            public void clearCache() {
                modelView.clearCache();
            }
        });
        setContentView(view.getRootView());

        //get an instance of our viewmodel and get things injected...
        modelView = MainActivityViewModel.create(this);
        MyApplication.getAppComponent().inject(modelView);

        //Let's observe the result and update our UI upon changes
        modelView.getResult().observe(this, repoResponseResource -> {
            view.loading(false);
            Log.d("Status ", "" + repoResponseResource.getStatus());
            switch (repoResponseResource.getStatus()) {
                case LOADING: {
                    view.refreshing(true);
                        view.bind(repoResponseResource.getData());
                    break;
                }
                case ERROR:
                    view.refreshing(false);
                    Log.e("Error", repoResponseResource.getException().getMessage(), repoResponseResource.getException());
                    Toast.makeText(MainActivity.this, R.string.error, Toast.LENGTH_LONG).show();
                    break;
                case SUCCESS: {
                    view.refreshing(false);
                        view.bind(repoResponseResource.getData());
                    break;
                }
            }
        });
        view.loading(true);
        modelView.load(page, LIMIT);
    }

    private void loadMoreObjects() {
        view.loading(true);

        modelView.load(++page, LIMIT);
    }


}
