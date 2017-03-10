package com.labs.poziom.whereabouts;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread timer = new Thread(){
            public void run()
            {
                try
                {

                  sleep(2500);
                }
                catch(InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally
                {


                    startActivity(new Intent(getApplicationContext(), NumberAuthLogin.class));


                }
            }



        };
        timer.start();



    }
}
