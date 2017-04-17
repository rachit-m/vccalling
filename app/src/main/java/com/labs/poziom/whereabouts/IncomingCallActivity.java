package com.labs.poziom.whereabouts;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.WindowManager;
import android.widget.TextView;

public class IncomingCallActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {

            // TODO Auto-generated method stub
            super.onCreate(savedInstanceState);

           // getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
           // getWindow().addFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);


            setContentView(R.layout.activity_incoming_call);


            String number = getIntent().getStringExtra(
                    TelephonyManager.EXTRA_INCOMING_NUMBER);
            TextView text = (TextView) findViewById(R.id.numtext);
            text.setText("Incoming call from " + number);
            setWindowParams();
        }
        catch (Exception e) {

            e.printStackTrace();
        }
    }
    public void setWindowParams() {
        WindowManager.LayoutParams wlp = getWindow().getAttributes();
        wlp.dimAmount = 0;
        wlp.flags = WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        getWindow().setAttributes(wlp);
    }
}
