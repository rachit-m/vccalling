package com.labs.poziom.whereabouts;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Rachit on 2/7/2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.PersonViewHolder>{

    public static class PersonViewHolder extends RecyclerView.ViewHolder {
        TextView personName;
        TextView personPhone;
        ImageView personPhoto;

        PersonViewHolder(View itemView) {
            super(itemView);
            personName = (TextView)itemView.findViewById(R.id.ssid);
            personPhone = (TextView)itemView.findViewById(R.id.alias);
          //  personPhoto = (ImageView)itemView.findViewById(R.id.person_photo);

        }
    }
    List<ContactModel> persons;

    RVAdapter(Context context, List<ContactModel> persons){
        this.persons = persons;
    }
    @Override
    public PersonViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.wifiprofile, viewGroup, false);
        PersonViewHolder pvh = new PersonViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PersonViewHolder personViewHolder, int i) {
        personViewHolder.personName.setText(persons.get(i).name);
        personViewHolder.personPhone.setText(persons.get(i).id);
      //  personViewHolder.personPhoto.setImageResource(persons.get(i).photoId);
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}