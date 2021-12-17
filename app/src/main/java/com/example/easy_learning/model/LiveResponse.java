package com.example.easy_learning.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LiveResponse {
    private String message;
    @SerializedName("data")
    private List<com.example.easy_learning.model.Live> liveList;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<com.example.easy_learning.model.Live> getLiveList() {
        return liveList;
    }

    public void setLiveList(List<com.example.easy_learning.model.Live> liveList) {
        this.liveList = liveList;
    }
}
