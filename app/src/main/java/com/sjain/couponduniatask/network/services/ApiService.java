package com.sjain.couponduniatask.network.services;

import com.sjain.couponduniatask.network.apimodel.APITaskResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by sanchitjain on 29/04/17.
 */
public interface ApiService {

    @GET("task")
    Call<APITaskResponse> getData(@Query("page") int pageNumber);

}
