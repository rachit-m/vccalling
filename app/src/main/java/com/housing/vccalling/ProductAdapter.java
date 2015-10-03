package com.housing.vccalling;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ProductAdapter extends ArrayAdapter<Product> {

    Context context;
    private ArrayList<Product> products;

    public ProductAdapter(Context context, int textViewResourceId, ArrayList<Product> items) {
        super(context, textViewResourceId, items);
        this.context = context;
        this.products = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.product, null);
        }
        Product o = products.get(position);
        if (o != null) {
            TextView name = (TextView) v.findViewById(R.id.name);
            TextView quantity = (TextView) v.findViewById(R.id.quantity);
            TextView price = (TextView) v.findViewById(R.id.price);
            TextView image = (TextView) v.findViewById(R.id.image);
            TextView username = (TextView) v.findViewById(R.id.username);
          //  TextView points = (TextView) v.findViewById(R.id.points);

            name.setText(String.valueOf(o.getName()));
            quantity.setText(String.valueOf(o.getQuantity()));
            price.setText(String.valueOf(o.getPrice()));
            image.setText(String.valueOf(o.getImage()));
            username.setText(String.valueOf(o.getUsername()));
            }
        return v;
    }
}