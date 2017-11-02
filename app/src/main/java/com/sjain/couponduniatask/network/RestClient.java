package com.sjain.couponduniatask.network;


import android.content.Context;

import com.sjain.couponduniatask.MyApp;
import com.sjain.couponduniatask.network.services.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by arjitagarwal on 30/09/15.
 */
public class RestClient {

    //App.DEBUG?"https://uatapi.justride.in/":"http://secure.justride.in/";
    private static final String BASE_URL = MyApp.BASEURL;
    private ApiService apiService;
    private Context context;

    public RestClient(Context context) {

        this.context = context;


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(MyApp.getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();


        apiService = retrofit.create(ApiService.class);

    }

    public ApiService getApiService() {
        return apiService;
    }

}
