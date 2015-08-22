package com.housing.vccalling;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.housing.vccalling.R;


public class OpeningActivity extends ActionBarActivity {

    TextView tvfrst,tvscnd,tvres;
    Button ok;
    EditText etfrst,etscnd,etpin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opening, menu);
        ok = (Button) findViewById(R.id.btnadd);
        tvres = (TextView) findViewById(R.id.tvres);
        etfrst = (EditText) findViewById(R.id.etfrst);
        etscnd = (EditText) findViewById(R.id.etscnd);
        etpin = (EditText) findViewById(R.id.etpin);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String x, y, z;

               /* if( (etfrst.getText().toString().matches("")) || (etscnd.getText().toString().matches("")) )
                {
                    Context ctx = getApplicationContext();
                  Toast.makeText(ctx,"Enter both the fields properly",Toast.LENGTH_SHORT).show();
                   // Toast.
                }*/

                if (TextUtils.isEmpty(etfrst.getText().toString())) {
                    etfrst.setError("Field can not be blank");
                } else if (TextUtils.isEmpty(etscnd.getText().toString())) {
                    etscnd.setError("Field can not be blank");
                } else {
                    x = etfrst.getText().toString();
                    y = etscnd.getText().toString();
                    new ServletPostAsyncTask().execute(new Pair<Context, String>(getBaseContext(), x) );
                    tvres.setText("Enter the pin");
                    etpin.setVisibility(View.VISIBLE);

                    new Servlet2().execute(new Pair<Context, String>(getBaseContext(),y));


                }
            }
        });
        etpin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
            if(s.length()==4)
            {
                tvres.setText("Pin complete");
                new ServletPostAsyncTask().execute(new Pair<Context, String>(getBaseContext(),s.toString()+etfrst.getText().toString()));

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
