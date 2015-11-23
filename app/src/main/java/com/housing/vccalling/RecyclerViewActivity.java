package com.housing.vccalling;

import android.app.Activity;
import android.os.AsyncTask;
//import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class RecyclerViewActivity extends Activity {

    private List<Person> persons;
    private RecyclerView rv;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    RecyclerView rcvEffect;
    RecvuAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        rv = (RecyclerView) findViewById(R.id.rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);
        initializeData();

        // specify an adapter (see also next example)






    }
    public class GetChat extends AsyncTask<String, Void, Void>
    {

        @Override
        protected Void doInBackground(String... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet("http://vccalling.appspot.com/sign?guestbookName="+params[0]);


            try {

                Toast.makeText(getApplicationContext(), "Success1", Toast.LENGTH_LONG).show();
                HttpResponse response = httpClient.execute(httpGet);
                if (response.getStatusLine().getStatusCode() == 200) {
                    Log.d("Backend", "Success");
                    Toast.makeText(getApplicationContext(), "Success", Toast.LENGTH_LONG).show();

                }
            }
            catch(Exception e)
            {

            }





            return null;
        }
    }


    public void initializeData(){

        persons = new ArrayList<>();

        Toast.makeText(getApplicationContext(), "Success0", Toast.LENGTH_LONG).show();

        String url = "http://vccalling.appspot.com/sign?guestbookName=Witty";

        // Request a string response

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String postkey = "";

                        // the response is already constructed as a JSONObject!
                        Log.d("Running","Site: ");
                        Iterator<String> iterator = response.keys();


                        while(iterator.hasNext())
                        {
                            postkey = iterator.next();
                            try {
                                Log.d("Running",response.get(postkey).toString());

                                persons.add(new Person(response.get(postkey).toString(),postkey , R.drawable.musica));

                            } catch (JSONException e) {
                                Log.d("Running","Not running");
                            }

                        }


                        mAdapter = new RVAdapter(persons);
                        rv.setAdapter(mAdapter);

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(this).add(jsonRequest);

      //  GetChat getChat = new GetChat();
      //  getChat.execute("helloworld");




     //  persons.add(new Person("Sai Prashanth Reddy", "SEO Manager", R.drawable.sai));
     //  persons.add(new Person("Sravan Bonthala", "Tech Expert", R.drawable.sravan));
      //  persons.add(new Person("Avi Jalan", "Operations Manager", R.drawable.avi));
    }






}
