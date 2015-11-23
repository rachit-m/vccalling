package com.example.android.effectivenavigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by raHuL on 9/11/2015.
 */
public class RecvuAdapter extends RecyclerView.Adapter<myViewholder>{


    private LayoutInflater inflate;
    private static Context context;
    private Bitmap image;
    private ArrayList<String> data = null;

    static Context getcontext()
    {
        return context;

    }

    public RecvuAdapter(Context ctx , ArrayList<String> dta,Bitmap pic)
    {
        context = ctx;
        inflate = LayoutInflater.from(ctx);
        this.data = dta;
        image = pic;
        //ImageEffects.setOriginalimage(image);
    }


    @Override
    public myViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = inflate.inflate(R.layout.recycle_layout,parent,false);
        myViewholder holder = new myViewholder(view);



        return holder;
    }

    @Override
    public void onBindViewHolder(myViewholder holder, int position) {
        String txt = data.get(position);
        holder.imgv.setImageBitmap(image); // need to change for as different pic have differnt effects
        holder.tv.setText(txt);

    }


    @Override
    public int getItemCount() {
        return data.size();
    }


}

class myViewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
   TextView tv;
   ImageView imgv;
   ViewPager vpgr;
    public myViewholder(View itemView) {

        super(itemView);

        itemView.setOnClickListener(this);
        tv = (TextView) itemView.findViewById(R.id.recvutv);
        imgv = (ImageView) itemView.findViewById(R.id.recvuimg);


    }


    @Override
    public void onClick(View v) {
        //Toast.makeText(v.getContext(),"GETTING ON CLICK"+getPosition(),Toast.LENGTH_LONG).show();
       // viewPager.setCurrentItem(0);

        View parent = v.getRootView();
        ViewPager viewPager = (ViewPager) parent.findViewById(R.id.viewpgid);
        TextView txt = (TextView) v.findViewById(R.id.recvutv);
        ImageView frg = (ImageView)v.findViewById(R.id.recvuimg);


        String chk = txt.getText().toString().substring(0, 5);

        if((chk=="FRAME") || (chk.equals("FRAME")))
        {
           // ;
            frg.buildDrawingCache();
            Bitmap frm = frg.getDrawingCache();
            ImageEffects.setFrame(frm);
        }

        //String xxx= txt.getText().toString();
        ImageEffects.setEffect_name(txt.getText().toString());
        viewPager.setCurrentItem(1);
            }
}
