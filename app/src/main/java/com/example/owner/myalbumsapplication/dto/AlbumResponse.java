package com.example.owner.myalbumsapplication.dto;

import com.google.gson.annotations.Expose;

import java.util.List;

public class AlbumResponse {
    final private int page;
    final private int limit;

    @Expose
    final private List<AlbumDto> list;

    public AlbumResponse(int page, int limit, List<AlbumDto> list) {
        this.page = page;
        this.limit = limit;
        this.list = list;
    }

    public int getPage() {
        return page;
    }

    public int getLimit() {
        return limit;
    }

    public List<AlbumDto> getList() {
        return list;
    }
}
