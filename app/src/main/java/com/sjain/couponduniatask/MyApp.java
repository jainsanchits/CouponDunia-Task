package com.sjain.couponduniatask;

import android.app.Application;
import android.content.Context;

import com.sjain.couponduniatask.network.RestClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by sjain on 31/10/17.
 */

public class MyApp extends Application {

    public static String BASEURL = "http://www.coupondunia.in/";

    //ApplicationContext
    private static Context mContext;

    //Restclient declartions
    private static RestClient restClient;

    private static OkHttpClient.Builder okHttpClient;

    public static RestClient getRestClient(Context context) {
        mContext = context.getApplicationContext();
        if (restClient == null) {
            restClient = new RestClient(mContext);
        }
        return restClient;
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder();
            okHttpClient.connectTimeout(60, TimeUnit.SECONDS);
            HttpLoggingInterceptor logger = new HttpLoggingInterceptor();
            logger.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClient.interceptors().add(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder().addHeader("user-agent", "Mozilla/5.0 (Linux; Android 4.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Mobile Safari/537.36");
                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });
            if (logger != null) {
                okHttpClient.interceptors().add(logger);
            }
        }
        return okHttpClient.build();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }


}
