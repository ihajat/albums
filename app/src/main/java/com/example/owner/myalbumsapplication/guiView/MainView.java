package com.example.owner.myalbumsapplication.guiView;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.owner.myalbumsapplication.R;
import com.example.owner.myalbumsapplication.databinding.ActivityMainBinding;
import com.example.owner.myalbumsapplication.dto.AlbumDto;
import com.example.owner.myalbumsapplication.ui.DetailedActivity;
import com.example.owner.myalbumsapplication.ui.listeners.ExtendableList;
import com.example.owner.myalbumsapplication.ui.listeners.RecyclerViewLoadMoreScrollListener;

import java.util.ArrayList;
import java.util.List;

public class MainView implements GuiView {
    private View rootView;
    private ActivityMainBinding binding;
    private RecyclerView listView;
    private EventHandler handler;

    public MainView(LayoutInflater inflater, ViewGroup vg, final Listener listener) {
        this.handler = new EventHandler(listener);
        binding = DataBindingUtil.inflate(inflater, R.layout.activity_main, vg, false);
        binding.setHandler(handler);
        rootView = binding.getRoot();
        listView = rootView.findViewById(R.id.list);
        listView.setLayoutManager(new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false));
        listView.setAdapter(new RepoAdapter(inflater));

        RecyclerViewLoadMoreScrollListener loadMoreScrollListener = new RecyclerViewLoadMoreScrollListener(new ExtendableList() {
            @Override
            public void loadMore() {
                if (listener != null) {
                    listener.loadMore();
                }
            }
        }, 100, 3);
        listView.addOnScrollListener(loadMoreScrollListener);
        loadMoreScrollListener.loadMoreIfneeded(listView);
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @Override
    public Bundle getViewState() {
        return null;
    }

    public void loading(boolean loading) {
        binding.setShowLoadMoreSpinner(loading);

    }

    public void refreshing(boolean refreshing) {
        binding.setRefreshing(refreshing);

    }

    public void bind(List<AlbumDto> list) {
        if (list == null) return;

        ((RepoAdapter) listView.getAdapter()).getAlbumDtoList().addAll(list);
        if (((RepoAdapter) listView.getAdapter()).getAlbumDtoList().size() != ((RepoAdapter) listView.getAdapter()).getItemCount()) {
            listView.getAdapter().notifyDataSetChanged();
        }
    }

    public interface Listener {

        void loadMore();

        void clearCache();
    }

    public class EventHandler {
        final private Listener listener;

        public EventHandler(Listener listener) {
            this.listener = listener;
        }

        public void onClearCacheClicked(View v) {
            if (listener != null) listener.clearCache();
        }
    }

    private class RepoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        final private LayoutInflater inflater;
        private List<AlbumDto> AlbumDtoList = new ArrayList<>();

        public List<AlbumDto> getAlbumDtoList() {
            return AlbumDtoList;
        }

        private RepoAdapter(LayoutInflater inflater) {
            this.inflater = inflater;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ItemView view = new ItemView(inflater, parent, new ItemView.Listener() {
            });
            ItemHolder holder = new ItemHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            AlbumDto AlbumDto = AlbumDtoList.get(position);
            ((ItemHolder) holder).bind(AlbumDto);

            holder.itemView.setOnClickListener(v -> {
                String title = ((TextView) listView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.txtTitle)).getText().toString();
                Intent intent = new Intent(rootView.getContext(), DetailedActivity.class);
                intent.putExtra("title", title);
                rootView.getContext().startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return AlbumDtoList.size();
        }

        public class ItemHolder extends RecyclerView.ViewHolder {
            ItemView holder;

            public ItemHolder(ItemView holder) {
                super(holder.getRootView());
                this.holder = holder;
            }

            public void bind(AlbumDto AlbumDto) {
                holder.bind(AlbumDto);
            }
        }
    }


}
