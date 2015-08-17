package com.housing.vccalling;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

/**
 * Created by Admin on 12-06-2015.
 */
public class Splash extends Activity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        MediaPlayer oursong = MediaPlayer.create(Splash.this,R.raw.musica_sound);
        oursong.start();
        Thread timer = new Thread(){
          public void run()
          {
              try
              {
                sleep(4500);
              }
              catch(InterruptedException e)
              {
                  e.printStackTrace();
              }
              finally
              {
                  Intent opnactvty = new Intent("com.example.admin.OPENINGACTIVITY");
                  startActivity(opnactvty);
              }
          }



        };
      timer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
