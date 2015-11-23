package com.housing.vccalling;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.appengine.repackaged.com.google.common.base.Flag;
import com.housing.vccalling.R;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class OpeningActivity extends ActionBarActivity {

    TextView tvfrst,tvscnd,tvres,etfrst;
    List<String> chat_characters = new ArrayList<String>();
    Button ok;
    EditText etscnd;

    Spinner etpin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_opening);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent i = new Intent(getApplicationContext(), com.housing.vccalling.EditImage.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
     //   Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
     //   intent.setComponent(new ComponentName("com.rupeevest.imgpro", "com.rupeevest.imgpro.EditImage"));
      //  startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_opening, menu);
        ok = (Button) findViewById(R.id.btnadd);


        tvres = (TextView) findViewById(R.id.tvres);
        etfrst = (TextView) findViewById(R.id.etfrst);
        etscnd = (EditText) findViewById(R.id.etscnd);
        etpin = (Spinner) findViewById(R.id.etpin);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.chat_tag, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        etpin.setAdapter(adapter);

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


               if (TextUtils.isEmpty(etscnd.getText().toString())) {
                    etscnd.setError("Field can not be blank");
                } else {
                    x = etfrst.getText().toString();
                    y = etscnd.getText().toString();

                    // This is for SMS
                  //  new ServletPostAsyncTask().execute(new Pair<Context, String>(getBaseContext(), x) );
                    tvres.setText("Tagging helps people search");

                    if(etpin.getVisibility() == View.VISIBLE)
                    {
                        Log.d("Running","Volley post after this");

                        String url = "http://vccalling.appspot.com/sign";

                        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        //Log.d("Running","Entered on response");
                                        //Intent i = new Intent(getApplicationContext(),EditImage.class);
                                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        //startActivity(i);



                                        //  Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
                                      //  intent.setComponent(new ComponentName("com.rupeevest.imgpro", "com.rupeevest.imgpro.EditImage"));
                                      //  startActivity(intent);

                                     //   Intent i = new Intent("com.housing.vccalling.RECARDVIEWACTIVITY");
                                      //  getApplicationContext().startActivity(i);

                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        error.printStackTrace();
                                    }
                                }
                        ) {
                            @Override
                            protected Map<String, String> getParams()
                            {
                                Map<String, String> params = new HashMap<>();
                                String query;


                                params.put("guestbookName",etpin.getSelectedItem().toString());


                                try {
                                    query = URLEncoder.encode(etscnd.getText().toString(), "utf-8");
                                }
                                catch(Exception e)
                                {   query = "";
                                    Log.d("Running","Exception in encoding");
                                }
                                params.put("content",query);
                                Log.d("Running",chat_characters.toString());
                                params.put("Characters",chat_characters.toString());
                                return params;

                            }
                        };



                      Volley.newRequestQueue(getApplicationContext()).add(postRequest);




                    }
                    else {
                        etpin.setVisibility(View.VISIBLE);

                    //This is for Emailer
                    new Servlet2().execute(new Pair<Context, String>(getBaseContext(),y));}


                }
            }
        });
        etpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


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
            

            // Creates a new text clip to put on the clipboard
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

            String pasteData = "",tempString = "",reformed_chat = "Conversation\n";


            Integer i = 0,j=0,k=0;
            if ((clipboard.hasPrimaryClip())) {
                ClipData.Item item_chat = clipboard.getPrimaryClip().getItemAt(0);
                pasteData = (String) item_chat.getText();

                //All initialisations happening here.

                String actual_chats[] = new String[20];
                String number_track[] = new String[20];
                String reformed_chats[] = new String[20];
                String chats[] =  pasteData.split(":");

                Map<String, String> m = new HashMap<String, String>();


               String characters[] = {"A","B","C","D"};

                for(String chat:chats)
                {actual_chats[i] = chat.split("\\r?\\n")[0];

                 // Log.d("Running",actual_chats[i]);
                    if(i%2 == 1)
                    {   tempString= chat.split("]")[1];
                        number_track[j] = tempString;
                        j++;}

                    i++;}
             //  List<String> subList = Arrays.asList(actual_chats);
                Set<String> mySet = new HashSet<String>(Arrays.asList(number_track));


                for (Iterator<String> it= mySet.iterator();it.hasNext();) {
                    m.put(it.next(),characters[k]);
                    k++;
                }

                //reforming chats with our characters

                i=0;
                for(String chat:chats)
                {

                    if(i%2 ==1) {
                        reformed_chats[i] = m.get(number_track[i / 2]);
                        reformed_chat += (reformed_chats[i] + ":");
                        chat_characters.add(reformed_chats[i]);

                    }
                    else {
                        reformed_chats[i] = actual_chats[i];
                        if(i>0)
                        reformed_chat += (actual_chats[i] + "\n");
                    }
                i++;

                }





               // Toast.makeText(getBaseContext(),reformed_chat , Toast.LENGTH_LONG).show();

                //actual chats from 1 onwards will be fine.

                etscnd.setText(reformed_chat);

            }


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
