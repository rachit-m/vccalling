package com.housing.vccalling;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.content.ContentValues;
import android.widget.EditText;
import android.widget.Toast;

import com.thrivecom.ringcaptcha.RingcaptchaApplication;
import com.thrivecom.ringcaptcha.RingcaptchaApplicationHandler;
import com.thrivecom.ringcaptcha.RingcaptchaVerification;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginButton = (Button) findViewById(R.id.btnStartService);
        loginButton.setOnClickListener(new View.OnClickListener()
        {
          @Override
          public void onClick(View V)
         {String x;
         RingcaptchaApplication.onboard(getApplicationContext(), "5i8o6eme1e8emi2yfu3i", "qufe8ygi9a4y6ometo4i", new RingcaptchaApplicationHandler() {
             @Override
             public void onSuccess(RingcaptchaVerification ringcaptchaVerification) {
                 Log.i("RingCaptcha", "Success");
                 Intent i;
                 i = new Intent( MainActivity.this , Register.class );
                 startActivity(i);


             }

             @Override
             public void onCancel() {
                 Log.i("RingCaptcha", "Cancel");



             }
         });
         }}
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void startForm(View view)
    {
        Intent i;
        i = new Intent( MainActivity.this , Register.class   );
        startActivity(i);

    }

}