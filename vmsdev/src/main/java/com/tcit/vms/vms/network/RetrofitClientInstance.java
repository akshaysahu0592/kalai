package com.tcit.vms.vms.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;
public class RetrofitClientInstance {
    private static Retrofit retrofit;
    public static Retrofit getRetrofitInstance(String baseUrl) {
        if (retrofit == null) {
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client
                    .connectTimeout(300, TimeUnit.SECONDS)
                    .readTimeout(300, TimeUnit.SECONDS)
                    .writeTimeout(300, TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
        }
        return retrofit;
    }
}

