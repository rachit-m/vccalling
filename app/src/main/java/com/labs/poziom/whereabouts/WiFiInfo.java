package com.labs.poziom.whereabouts;

/**
 * Created by Rachit on 2/21/2017.
 */

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Rachit on 12/8/2016.
 */
public interface WiFiInfo {

    @GET("http://52.27.54.85/blog/rad_app/wifi_connect.php")
    Call<String> knowWiFi(@Query("user_contact") String contact);

}
