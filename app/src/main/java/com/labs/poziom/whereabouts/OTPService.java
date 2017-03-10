package com.labs.poziom.whereabouts;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Rachit on 12/8/2016.
 */
public interface OTPService {

    @GET("http://52.27.54.85/blog/rad_app/otp.php")
    Call<String> sendOTP(@Query("user_contact") String contact);

}
