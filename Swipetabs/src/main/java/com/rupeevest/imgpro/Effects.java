package com.rupeevest.imgpro;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Admin on 10-09-2015.
 */
public class Effects extends Fragment {

    private List<Person> persons;
    private RecyclerView rv;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rcvEffect;
    RecvuAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            Log.d("STATUS", "onCreate called for the first time EFFECTS");
        } else {
            Log.d("EFFECTS", "onCreate called for the second time EFFECTS");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("STATUS", "ONCREATEVIEW CAlled in EFFECT");

        View layout = inflater.inflate(R.layout.effects, container, false);
        rv = (RecyclerView) layout.findViewById(R.id.rv);


        rv.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity());
        rv.setLayoutManager(mLayoutManager);
        initializeData();
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d("STATUS", "onResume is called....for Effects");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("STATUS", "onState Restored in EFFECTS");
    }

    public void initializeData() {

        persons = new ArrayList<>();


        String url = "http://vccalling.appspot.com/sign?guestbookName=Witty";

        // Request a string response

        JsonObjectRequest jsonRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String postkey = "";

                        // the response is already constructed as a JSONObject!
                        Log.d("Running", "Site: ");
                        Iterator<String> iterator = response.keys();


                        while (iterator.hasNext()) {
                            postkey = iterator.next();
                            try {
                                Log.d("Running", response.get(postkey).toString());

                                persons.add(new Person(response.get(postkey).toString(), postkey, R.drawable.champa));

                            } catch (JSONException e) {
                                Log.d("Running", "Not running");
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

        Volley.newRequestQueue(this.getActivity()).add(jsonRequest);


    }
}
