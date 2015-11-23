package com.example.android.effectivenavigation;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;

/**
 * Created by Admin on 10-09-2015.
 */
public class Effects extends Fragment
{


    RecyclerView rcvEffect;
    RecvuAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState==null)
        {
            Log.d("STATUS", "onCreate called for the first time EFFECTS");
        }
        else
        {
            Log.d("EFFECTS", "onCreate called for the second time EFFECTS");
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("STATUS", "ONCREATEVIEW CAlled in EFFECT");

        View layout = inflater.inflate(R.layout.effects,container,false);
        rcvEffect = (RecyclerView)layout.findViewById(R.id.rcv_effect);

        ArrayList<String> data = new ArrayList<String>();
        data.add("SEPIA_RED");
        data.add("SEPIA_GREEN");
        data.add("SEPIA_VIOLET");
        data.add("SEPIA_BLUE");
        data.add("SEPIA_YELLOW");
        data.add("SEPIA_DEEP_VIOLET");
        data.add("SEPIA_CUSTOM_1");
        data.add("MONOCROME");
        data.add("SMOOTH");
       // data.add("MEAN_REMOVAL");
        data.add("SHARP");
        data.add("BLUR");
        data.add("EMBOSS");
        data.add("BRIGHTNESS");
        data.add("CONTRAST");

        Drawable myDrawable = getResources().getDrawable(R.drawable.testimage);
        Bitmap myLogo = ((BitmapDrawable) myDrawable).getBitmap();

        adapter = new RecvuAdapter(getActivity().getApplicationContext(),data,myLogo);

        rcvEffect.setAdapter(adapter);
       // rcvEffect.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcvEffect.setLayoutManager(new GridLayoutManager(getActivity(),3));
       // rcvEffect.setLayoutManager( new GridLayoutManager(getActivity().getApplicationContext()));
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

}
