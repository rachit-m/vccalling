package com.housing.vccalling;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;

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
              { //Intent opnactvty = new Intent("com.housing.vccalling.RECARDVIEWACTIVITY");
                 Intent opnactvty = new Intent("com.housing.vccalling.OPENINGACTIVITY");
                  Log.d("Intent", opnactvty.getAction());
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
