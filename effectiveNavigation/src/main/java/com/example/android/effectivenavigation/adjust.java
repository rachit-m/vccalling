package com.example.android.effectivenavigation;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by Admin on 10-09-2015.
 */
public class adjust extends Fragment
{
    RecyclerView rv;
    private static ImageView imv;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

          if(savedInstanceState==null)
          {
              //Log.d("ON_CREATE", "onCreate called for the first time ADjust");
              Log.d("STATUS", "onCreate first time");
          }
        else
          {
              Log.d("STATUS", "onCreate 2nd time");
          }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("STATUS", "ONCREATEVIEW CAlled in ADJUST");

        View layout = inflater.inflate(R.layout.adjust,container,false);
        imv = (ImageView) layout.findViewById(R.id.imgvuadjst);
        imv.setImageBitmap(ImageEffects.getOriginalimage());
        return layout;



    }


    @Override
    public void onPause() {
        super.onPause();
        Log.d("STATUS", "onCreate 2nd time");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("STATUS", "onCreate 2nd time");

    }

    @Override
    public void onResume() {

        super.onResume();

        //LayoutInflater inflator = (LayoutInflater) getActivity().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        Log.d("STATUS", "onRESUME in ADJUST");

        if(ImageEffects.getEffect_name()!=null)
        {

            Log.d("STATUS", "onRESUME in ADJUST INSIDE");

            final Context ctx = getActivity().getApplicationContext();

            class addeffect extends AsyncTask<String, Integer, Bitmap> {


                @Override
                protected Bitmap doInBackground(String... params) {

                    Bitmap bmp=null;
                    if(ImageEffects.getEffect_name()=="SEPIA_RED")
                    {
                        bmp = ImageEffects.createSepiaToningEffect(2, 60, 30, 30);
                        ImageEffects.setEffect_name(null);
                    }
                    if(ImageEffects.getEffect_name()=="SEPIA_GREEN")
                    {
                        bmp = ImageEffects.createSepiaToningEffect(2, 30, 60, 30);
                        ImageEffects.setEffect_name(null);
                    }
                    if(ImageEffects.getEffect_name()=="SEPIA_VIOLET")
                    {
                        bmp = ImageEffects.createSepiaToningEffect(2, 30, 30, 60);
                        ImageEffects.setEffect_name(null);
                    }
                    if(ImageEffects.getEffect_name()=="SEPIA_BLUE")
                    {
                        bmp = ImageEffects.createSepiaToningEffect(2, 0, 60, 60);
                        ImageEffects.setEffect_name(null);
                    }
                    if(ImageEffects.getEffect_name()=="SEPIA_YELLOW")
                    {
                        bmp = ImageEffects.createSepiaToningEffect(2, 60, 60, 0);
                        ImageEffects.setEffect_name(null);
                    }
                    if(ImageEffects.getEffect_name()=="SEPIA_DEEP_VIOLET")
                    {
                        bmp = ImageEffects.createSepiaToningEffect(2, 60, 0, 60);
                        ImageEffects.setEffect_name(null);
                    }
                    if(ImageEffects.getEffect_name()=="SEPIA_CUSTOM_1")
                    {
                        bmp = ImageEffects.createSepiaToningEffect(9, 0,15,60);
                        ImageEffects.setEffect_name(null);
                    }


                    if(ImageEffects.getEffect_name()=="MONOCROME")
                    {
                        bmp = ImageEffects.CreateGreyscale();
                        ImageEffects.setEffect_name(null);
                    }

                    if(ImageEffects.getEffect_name()=="SMOOTH")
                    {
                        bmp = ImageEffects.smooth();
                        ImageEffects.setEffect_name(null);
                    }
                    if(ImageEffects.getEffect_name()=="MEAN_REMOVAL")
                    {
                      //  bmp = ImageEffects.CreateGreyscale();
                        ImageEffects.setEffect_name(null);
                    }
                    if(ImageEffects.getEffect_name()=="SHARP")
                    {
                        bmp = ImageEffects.CreateSharpen();
                        ImageEffects.setEffect_name(null);
                    }

                    if(ImageEffects.getEffect_name()=="BLUR")
                    {
                          bmp = ImageEffects.CreateBlur();
                        ImageEffects.setEffect_name(null);
                    }

                    if(ImageEffects.getEffect_name()=="EMBOSS")
                    {
                          bmp = ImageEffects.Emboss();
                        ImageEffects.setEffect_name(null);
                    }
                    if(ImageEffects.getEffect_name()=="BRIGHTNESS")
                    {
                         bmp = ImageEffects.Brightness(4); // value can be adjusted for darker or lighewr image
                        ImageEffects.setEffect_name(null);
                    }
                    if(ImageEffects.getEffect_name()=="CONTRAST")
                    {
                        bmp = ImageEffects.Contrast(5); // value can be adjusted for darker or lighewr image
                        ImageEffects.setEffect_name(null);
                    }

                    if(ImageEffects.getEffect_name()!=null)
                    {

                        if (ImageEffects.getEffect_name().substring(0, 5).equals("FRAME")) {
                            //Log.d("STATUS", "IN ADJUST Frame_Name-->"+);
                            bmp = ImageEffects.Overlay(ImageEffects.getFrame(),ImageEffects.getOriginalimage() , getActivity().getApplicationContext());
                            ImageEffects.setEffect_name(null);
                            ImageEffects.setFrame(null);
                        }
                    }


                    return bmp;
                }

                @Override
                protected void onPostExecute(Bitmap img) {
                    super.onPostExecute(img);

                    //View v = (View) getView().findViewById(R.id.frame_main);

                   imv.setImageBitmap(img);


                  /*  LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    // if(inflater != null)
                    {
                        View v = inflater.inflate(R.layout.adjust, null);
                        ImageView imv = (ImageView) v.findViewById(R.id.imgvuadjst);
                        imv.setImageBitmap(img);

                    }
                  */
                    // FragmentActivity fa = getActivity();

                    // Fragment a = getFragmentManager().findFragmentById(R.id.adjust_frgmnt);
                    // View v = a.getView();

                }

                @Override
                protected void onProgressUpdate(Integer... values) {
                    super.onProgressUpdate(values);
                }
            }

            addeffect obj = new addeffect();
            obj.execute(ImageEffects.getEffect_name());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d("STATUS", "onState Restored");
    }


}
