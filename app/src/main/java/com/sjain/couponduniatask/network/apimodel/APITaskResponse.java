package com.sjain.couponduniatask.network.apimodel;

import com.google.gson.annotations.SerializedName;
import com.sjain.couponduniatask.model.TaskResponse;

/**
 * Created by sjain on 31/10/17.
 */

public class APITaskResponse {
    @SerializedName("success")
    private Boolean success;
    @SerializedName("authenticated")
    private Boolean authenticated;
    @SerializedName("response")
    private TaskResponse response;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Boolean getAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(Boolean authenticated) {
        this.authenticated = authenticated;
    }

    public TaskResponse getResponse() {
        return response;
    }

    public void setResponse(TaskResponse response) {
        this.response = response;
    }

}
