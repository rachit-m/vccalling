package com.labs.poziom.whereabouts;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Rachit on 12/8/2016.
 */

public interface RegisterService {
    @FormUrlEncoded
    @POST("http://52.27.54.85/blog/rad_app/reg_api.php")
    Call<String> registeruser(@Field("contact") String contact, @Field("password") String password, @Field("otp") Integer otp, @Field("android_id") String android_id);

}

