package com.labs.poziom.whereabouts;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Sandy on 13-01-2017.
 */

public interface ApiInterface {
    /*  @GET("/otp.php")
      Call<String> sendOTP(@Query("user_contact") String contact);*/

    @FormUrlEncoded
    @POST("wifi_info.php")
    Call<String> createTask(
            @Field("user_contact") String contact,
            @Field("ssid") String ssid,
            @Field("bssid") String bssid,
            @Field("password") String password);
}
