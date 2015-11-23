package com.housing.vccalling;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

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
    private RecyclerView dynamic;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.Adapter dAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    RecyclerView rcvEffect;
    //RecvuAdapter adapter;

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

        LinearLayout k = (LinearLayout) layout.findViewById(R.id.effect_f1);

        dynamic = new RecyclerView(this.getActivity());

        ViewGroup.LayoutParams params = dynamic.getLayoutParams();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        dynamic.setLayoutParams(params);

      //  dynamic.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    //    dynamic.setHasFixedSize(true);

        rv = (RecyclerView) layout.findViewById(R.id.rv);
      //  lvTest = (TwoWayView) layout.findViewById(R.id.lvItems);


        rv.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this.getActivity(),LinearLayoutManager.HORIZONTAL,false);
       // mLayoutManager.canScrollHorizontally();
        //mLayoutManager = new LinearLayoutManager(this.getActivity(),CustomLinearLayoutManager.HORIZONTAL, true  );
        dynamic.setLayoutManager(new LinearLayoutManager(this.getActivity(),LinearLayoutManager.HORIZONTAL,false));
        rv.setLayoutManager(mLayoutManager);
        initializeData();
        k.addView(dynamic);
        Log.d("Running","Dynamic Views");
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

                                persons.add(new Person(response.get(postkey).toString(), postkey, R.drawable.musica));

                            } catch (JSONException e) {
                                Log.d("Running", "Not running");
                            }

                        }


                        mAdapter = new com.housing.vccalling.RVAdapter(persons);
                        dAdapter = new RVAdapter(persons);
                       rv.setAdapter(mAdapter);
                        dynamic.setAdapter(dAdapter);


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
