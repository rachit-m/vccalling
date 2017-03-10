package com.labs.poziom.whereabouts;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by Sandy on 13-01-2017.
 */

public class MyJobService extends JobService {

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        SharedPreferences sharedPreferences=getSharedPreferences("phone",Context.MODE_PRIVATE);
        ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conMan.getActiveNetworkInfo();
        if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Log.i("tag", "onstartjob");
            WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            String ssid = wifiInfo.getSSID();
            Log.d("SSID", ssid);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<String> call = apiService.createTask(sharedPreferences.getString("phone","9999999999"), ssid, "38-f8-89-0f-8a-c2", "mypassword123");
            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    Log.e(onWifiReceiver.class.getSimpleName(), response.body() + " onresponse");
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Log error here since request failed
                    Log.e("failed", t.toString());
                }
            });


        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
