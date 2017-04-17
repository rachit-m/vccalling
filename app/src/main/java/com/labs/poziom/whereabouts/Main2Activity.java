package com.labs.poziom.whereabouts;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Main2Activity extends AppCompatActivity {

    Uri uri;
    WiFiInfo service;
    Call<String> call;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    private static final int REQUEST_PHONE_CALL = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Typeface tf = Typeface.createFromAsset(this.getAssets(),"fontawesome-webfont.ttf");


        SpannableString content = new SpannableString( getIntent().getStringExtra("name"));
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        ((TextView) findViewById(R.id.payname)).setText(content);

        content = new SpannableString( getIntent().getStringExtra("phone"));
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        ((TextView) findViewById(R.id.payphone)).setText(content);



       // ((TextView) findViewById(R.id.paytype)).setText("ID     :"+getIntent().getStringExtra("type"));

        service = retrofit.create(WiFiInfo.class);
        String spacefreePhone = getIntent().getStringExtra("phone").toString().replace(" ","");
        call = service.knowWiFi(spacefreePhone.substring(spacefreePhone.length()-10));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body().equals("unknown"))
                {

                    ((TextView) findViewById(R.id.paymmid)).setText("User not on App");

                }
                else
                {
           //         SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
              //      String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                    long min =  1800;
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                    Date currentLocalTime = cal.getTime();
                    try {
                        Date lastSeenTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(response.body());
                      min =   ((currentLocalTime.getTime() - lastSeenTime.getTime())/(1000*60)) - 330;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //  Date d = new Date(response.body());
                    ((TextView) findViewById(R.id.paymmid)).setText("Plugged "+ min + " minutes ago");

                }


            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                ((TextView) findViewById(R.id.paymmid)).setText("Cannot collect WiFi info");

            }
        });



        FontManager clickButton = (FontManager) findViewById(R.id.userWhatsapp);
            clickButton.setOnClickListener( new View.OnClickListener() {

                @Override
                public void onClick(View v) {
              /*      Intent intent = new Intent();
                   intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("content://com.android.contacts/data/"+getIntent().getStringExtra("type")),
                            "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                    intent.setPackage("com.whatsapp");
*/

                    Uri uri = Uri.parse("smsto:" + "+91"+ getIntent().getStringExtra("phone").substring(getIntent().getStringExtra("phone").length()-10));
                    Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                    i.putExtra("sms_body", getIntent().getStringExtra("name"));
                    i.setPackage("com.whatsapp");
                    startActivity(i);
                }
        });

        FontManager clickButton1 = (FontManager) findViewById(R.id.userCall);
        clickButton1.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
              /*      Intent intent = new Intent();
                   intent.setAction(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.parse("content://com.android.contacts/data/"+getIntent().getStringExtra("type")),
                            "vnd.android.cursor.item/vnd.com.whatsapp.voip.call");
                    intent.setPackage("com.whatsapp");
*/
                Uri uri = Uri.parse("tel:" + getIntent().getStringExtra("phone"));
                Intent intnt = new Intent(Intent.ACTION_CALL,uri);
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(Main2Activity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Main2Activity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
                    }
                    else
                    {
                        startActivity(intnt);
                    }
                }
                else
                {
                    startActivity(intnt);
                }


            }
        });
    }


}
