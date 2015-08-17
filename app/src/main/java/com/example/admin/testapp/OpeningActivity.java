package com.example.admin.testapp;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class OpeningActivity extends ActionBarActivity {

    TextView tvfrst,tvscnd,tvres;
    Button ok;
    EditText etfrst,etscnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);

        new ServletPostAsyncTask().execute(new Pair<Context, String>(this, "Rachit"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opening, menu);
        ok = (Button) findViewById(R.id.btnadd);
        tvres = (TextView) findViewById(R.id.tvres);
        etfrst = (EditText) findViewById(R.id.etfrst);
        etscnd = (EditText) findViewById(R.id.etscnd);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double x=0,y=0,z=0;
               /* if( (etfrst.getText().toString().matches("")) || (etscnd.getText().toString().matches("")) )
                {
                    Context ctx = getApplicationContext();
                  Toast.makeText(ctx,"Enter both the fields properly",Toast.LENGTH_SHORT).show();
                   // Toast.
                }*/

                if(TextUtils.isEmpty(etfrst.getText().toString()))
                {
                    etfrst.setError("Field can not be blank");
                }
                else if(TextUtils.isEmpty(etscnd.getText().toString()))
                {
                    etscnd.setError("Field can not be blank");
                }
                else
                {
                          x = Double.parseDouble(etfrst.getText().toString());
                          y = Double.parseDouble(etscnd.getText().toString());
                          z = x + y;
                    tvres.setText("Result is" + z);
                }
            }
        });
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
}
