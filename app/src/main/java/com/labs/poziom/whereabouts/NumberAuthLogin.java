package com.labs.poziom.whereabouts;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NumberAuthLogin extends AppCompatActivity {

    SharedPreferences sharedPreferences ;
  //  private String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
   // TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    EditText phone_number;
    EditText password;
    EditText otp;
    EditText wifi;
    OTPService service;
    Call<String> call;
    TextView errorHandler;
    ProgressBar prog_bar;
    LinearLayout layoutnum;
    LinearLayout layoutotp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        sharedPreferences = getSharedPreferences("phone", Context.MODE_PRIVATE);

       /* SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();*/


            if (sharedPreferences.getString("phone", "notregistered") == "notregistered") {

                setContentView(R.layout.numberauth);
                prog_bar = (ProgressBar) findViewById(R.id.login_progress);
                errorHandler = (TextView) findViewById(R.id.errorhandler);

            } else {
                startActivity(new Intent(this, Contacts.class));
            }
            layoutnum = (LinearLayout) findViewById(R.id.email_login_form);
            layoutotp = (LinearLayout) findViewById(R.id.plus_sign_out_buttons);
            wifi = (EditText) findViewById(R.id.alias);


            phone_number = (EditText) findViewById(R.id.contact_num);
            password = (EditText) findViewById(R.id.password);
            otp = (EditText) findViewById(R.id.otp);
    }

    public void sendOTP(View v) {
        service = retrofit.create(OTPService.class);
        call = service.sendOTP(phone_number.getText().toString());
        prog_bar.setVisibility(View.VISIBLE);
        errorHandler.setVisibility(View.GONE);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (!response.body().equals("new_user")) {

                    if(response.body().equals(Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID)))
                    {   SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("phone", phone_number.getText().toString());
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(), Contacts.class));
                    }
                    else {

                        errorHandler.setText("User already registered");
                        errorHandler.setVisibility(View.VISIBLE);
                        prog_bar.setVisibility(View.GONE);
                        phone_number.setText("");
                        password.setText("");
                    }
                } else {
                    layoutnum.setVisibility(View.GONE);
                    layoutotp.setVisibility(View.VISIBLE);
                    prog_bar.setVisibility(View.GONE);
                }

            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                prog_bar.setVisibility(View.GONE);
                errorHandler.setText("No Internet Available");
                errorHandler.setVisibility(View.VISIBLE);
            }
        });

    }

    public void startMainActivity(View v) {
        RegisterService service = retrofit.create(RegisterService.class);
        Call<String> call = service.registeruser(phone_number.getText().toString(), password.getText().toString(), Integer.parseInt(otp.getText().toString()),  Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID));
        prog_bar.setVisibility(View.VISIBLE);
        errorHandler.setVisibility(View.GONE);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                prog_bar.setVisibility(View.GONE);
                if (response.body().equals("incorrect")) {
                    errorHandler.setVisibility(View.VISIBLE);
                } else {


                    editor.putString("phone", phone_number.getText().toString());
                    editor.commit();

               /*     AlarmManager am=(AlarmManager)getSystemService(Context.ALARM_SERVICE);
                    Intent in = new Intent(getApplicationContext(), onWifiReceiver.class);
                    //      in.putExtra(ONE_TIME, Boolean.TRUE);
                    PendingIntent pi = PendingIntent.getBroadcast(getApplicationContext(), 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
                    am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (10 * 1 * 1), pi);
*/

                    startActivity(new Intent(getApplicationContext(), Contacts.class));
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }


    public void Resend_OTP(View v) {
        Call<String> call1 = service.sendOTP(phone_number.getText().toString());
        errorHandler.setVisibility(View.GONE);
        call1.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                layoutotp.setVisibility(View.VISIBLE);
                layoutnum.setVisibility(View.GONE);
                prog_bar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                prog_bar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "No Internet Available", Toast.LENGTH_SHORT).show();
            }

        });
    }
}
