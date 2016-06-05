package com.shivamdev.twitterapidemo.network;

import com.shivamdev.twitterapidemo.Constants;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by shivamchopra on 04/06/16.
 */
public class RetrofitAdapter {
    private static final String TAG = RetrofitAdapter.class.getSimpleName();
    private static final String AUTHORIZATION = "Authorization";

    private Retrofit retrofit;
    private OkHttpClient okHttpClient;
    private static RetrofitAdapter adapter;

    private RetrofitAdapter() {
        retrofit = retrofit();
        okHttpClient = getOkHttpClient();
    }

    public static synchronized RetrofitAdapter get() {
        if (adapter == null) {
            adapter = new RetrofitAdapter();
        }
        return adapter;
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    private Retrofit retrofit() {
        return new Retrofit.Builder()
                .baseUrl(Constants.TWITTER_SEARCH_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create(GsonUtil.getInstance().getGson()))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        OkHttpClient client = new OkHttpClient.Builder()
                //.addInterceptor(logging)
                .build();
        return client;
    }
}
