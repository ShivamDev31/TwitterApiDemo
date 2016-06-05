package com.shivamdev.twitterapidemo.network;

import com.shivamdev.twitterapidemo.Constants;
import com.shivamdev.twitterapidemo.twitter.TwitterData;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by shivamchopra on 04/06/16.
 */
public interface TwitterApi {

    // twitter search api
    // https://api.twitter.com/1.1/search/tweets.json?q=%23freebandnames&since_id=24012619984051000&max_id=250126199840518145&result_type=mixed&count=4

    @Headers("Authorization:" + Constants.TWITTER_TOKEN)
    @GET("/1.1/search/tweets.json")
    Observable<TwitterData> getTweetsData(@QueryMap Map<String, Object> map);

}
