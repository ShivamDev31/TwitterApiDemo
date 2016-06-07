package com.shivamdev.apidemo.restaurant;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivamchopra on 06/06/16.
 */
public class RestaurantData {

  /*  {
        "restaurant_name": "Spill",
            "address": "J-349, JP Road, Opp. Apna Bazar, JP Rd, D.N.Nagar, Andheri West, Mumbai, Maharashtra, India",
            "latitude": "19.127473",
            "longitude": "72.832545",
            "phone_number": "+91 22 2642 5895",
            "logo_url": "http://image6.buzzintown.com/files/venue/upload_20000/upload_original/484402-spill-resto-bar.jpg"
    }*/


    @SerializedName("restaurant_name")
    public String restaurantName;

    @SerializedName("address")
    public String address;

    @SerializedName("latitude")
    public String lat;

    @SerializedName("longitude")
    public String lng;

    @SerializedName("phone_number")
    public String number;

    @SerializedName("logo_url")
    public String logoUrl;

}