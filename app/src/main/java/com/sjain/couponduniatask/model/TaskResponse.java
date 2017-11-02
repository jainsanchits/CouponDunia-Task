package com.sjain.couponduniatask.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sjain on 31/10/17.
 */

public class TaskResponse {
    @SerializedName("list")
    private ArrayList<ItemTask> list = null;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("total_items")
    private Integer totalItems;

    public ArrayList<ItemTask> getList() {
        return list;
    }

    public void setList(ArrayList<ItemTask> list) {
        this.list = list;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
}
