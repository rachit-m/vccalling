package com.labs.poziom.whereabouts;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Observable;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sandy on 19-01-2017.
 */

public class ContactsAdapter extends ArrayAdapter<ContactModel> {
    Context context1;
    Uri uri;
    WiFiInfo service;
    Call<String> call;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public ContactsAdapter(Context context, ArrayList<ContactModel> contact) {
        super(context, 0, contact);
        context1 = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        final ContactModel cm = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.mostimp, parent, false);
        }
        // Lookup view for data population
        final TextView tvName = (TextView) convertView.findViewById(R.id.trustname);
        TextView tvAlias=(TextView)  convertView.findViewById(R.id.trustphone);
        final ImageView tvOffline=(ImageView)  convertView.findViewById(R.id.trustwifioff);
        final ImageView tvActive=(ImageView)  convertView.findViewById(R.id.trustwifion);
        final TextView tvApp=(TextView)  convertView.findViewById(R.id.notonapp);


        // Populate the data into the template view using the data object
        tvName.setText(cm.getName());
        tvAlias.setText(cm.getPhone());


        service = retrofit.create(WiFiInfo.class);
        String spacefreePhone = cm.getPhone().replace(" ","");

        call = service.knowWiFi(spacefreePhone.substring(spacefreePhone.length()-10));
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {

                if (response.body().equals("unknown"))
                {
                    tvActive.setVisibility(View.INVISIBLE);
                    tvOffline.setVisibility(View.INVISIBLE);
                    tvApp.setVisibility(View.VISIBLE);


                }
                else
                {
                    //         SimpleDateFormat parser = new SimpleDateFormat("HH:mm");
                    //      String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());

                    long min =  1800;
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                    Date currentLocalTime = cal.getTime();
                    try {
                        Date lastSeenTime = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(response.body());
                        min =   ((currentLocalTime.getTime() - lastSeenTime.getTime())/(1000*60)) - 330;
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //  Date d = new Date(response.body());
                    if(min < 10) {
                    tvActive.setVisibility(View.VISIBLE);
                    tvOffline.setVisibility(View.INVISIBLE);
                        tvApp.setVisibility(View.INVISIBLE);

                    }
                    else
                    {
                        tvActive.setVisibility(View.INVISIBLE);
                        tvOffline.setVisibility(View.VISIBLE);
                        tvApp.setVisibility(View.INVISIBLE);

                    }

                }


            }


            @Override
            public void onFailure(Call<String> call, Throwable t) {
                tvActive.setVisibility(View.INVISIBLE);
                tvOffline.setVisibility(View.INVISIBLE);

            }
        });


        tvApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:" + ((TextView)((RelativeLayout)view.getParent()).findViewById(R.id.trustphone)).getText().toString());
                Intent i = new Intent(Intent.ACTION_SENDTO, uri);
                i.putExtra("sms_body", "Download VC Calling for better call experiences. https://play.google.com/store/apps/details?id=com.labs.poziom.whereabouts");
             //   i.setPackage("com.whatsapp");
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context1.startActivity(i);
            }
        });


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context1, Main2Activity.class);
                i.putExtra("name", cm.getName());
               i.putExtra("phone", cm.getPhone().replace(" ",""));
                i.putExtra("type", cm.getId());
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context1.startActivity(i);

            }
        });
        // Return the completed view to render on screen
        return convertView;
    }
  /*  private void startPolling() {
        pollDeliveries = Observable.interval(POLLING_INTERVAL, TimeUnit.SECONDS, Schedulers.from(AsyncTask.THREAD_POOL_EXECUTOR))
                .flatMap(tick -> getDeliveriesObs())
                .doOnError(err -> Log.e("MPB", "Error retrieving messages" + err))
                .retry()
                .subscribe(this::parseDeliveries, Throwable::printStackTrace);
    }*/


}