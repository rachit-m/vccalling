package com.rupeevest.imgpro;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Admin on 10-09-2015.
 */
public class frame extends Fragment
{
    Bitmap bmp;
    RecyclerView rcvFrame;
    Context ctx;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        Log.d("STATUS", "ONCREATEVIEW CAlled in FRAME");



        RecvuAdapter adapter;
        View layout = inflater.inflate(R.layout.frame,container,false);
         rcvFrame = (RecyclerView)layout.findViewById(R.id.rcv_frame);
        ///////
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Click is working",Toast.LENGTH_LONG).show();
            }
        });

        ArrayList<String> data = new ArrayList<String>();
        data.add("FRAME_1");
        data.add("FRAME_2");
        data.add("FRAME_3");

        Drawable myDrawable = getResources().getDrawable(R.drawable.frame);
        Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();

        adapter = new RecvuAdapter(getActivity().getApplicationContext(),data,myLogo);


        //rcvFrame.setLayoutManager( new LinearLayoutManager(getActivity()));
        rcvFrame.setLayoutManager(new GridLayoutManager(getActivity(),3));
        rcvFrame.setItemAnimator(new DefaultItemAnimator());
        rcvFrame.setAdapter(adapter);

        return layout;
    }





    @Override
    public void onResume() {
        super.onResume();

        Log.d("STATUS", "onResume is called....Frame");
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("STATUS", "onState Restored FRAME");
    }

}
