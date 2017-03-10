package com.poziomlabs.plugged;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;


public class LoginActivity extends ActionBarActivity {

    SharedPreferences sharedPreferences;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





        sharedPreferences = getSharedPreferences("splash", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Uno_Test","Office");

        editor.commit();



        if (sharedPreferences.getString("phone", "notregistered") == "notregistered") {


            setContentView(R.layout.activity_login);
        }
        else
        {
            startActivity(new Intent(this, MainActivity.class));

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startMainActivity(View v)
    {




        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();

        editor.putString("phone",((EditText)(((LinearLayout)v.getParent()).getChildAt(0))).getText().toString());

        editor.commit();
        startActivity(new Intent(this, MainActivity.class));

    }
}
