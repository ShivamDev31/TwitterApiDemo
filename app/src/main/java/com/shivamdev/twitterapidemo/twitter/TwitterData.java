package com.shivamdev.twitterapidemo.twitter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shivamchopra on 04/06/16.
 */
public class TwitterData {

    @SerializedName("statuses")
    public List<Statuses> statuses;

    public static class Statuses {

        @SerializedName("created_at")
        public String createdAt;

        @SerializedName("text")
        public String tweetText;

        @SerializedName("user")
        public User user;
    }

    public static class User {
        @SerializedName("name")
        public String userName;

        @SerializedName("location")
        public String userLocation;

        @SerializedName("profile_image_url")
        public String userImageUrl;
    }

}