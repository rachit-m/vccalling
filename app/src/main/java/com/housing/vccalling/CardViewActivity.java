package com.housing.vccalling;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import org.json.JSONException;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class CardViewActivity extends Activity {


    private static final String DEBUG_TAG = "HttpExample";
    ArrayList<Product> products = new ArrayList<Product>();
    ListView listview;
    Button btnDownload;

    TextView personName;
    TextView personAge;
    ImageView personPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_card_view);
        personName = (TextView)findViewById(R.id.person_name);
        personAge = (TextView)findViewById(R.id.person_age);
        personPhoto = (ImageView)findViewById(R.id.person_photo);

       listview = (ListView) findViewById(R.id.listview);
        btnDownload = (Button) findViewById(R.id.btnDownload);
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            btnDownload.setEnabled(true);
        } else {
            btnDownload.setEnabled(false);
        }

        personName.setText("Sai Prashanth Reddy");
        personAge.setText("SEO Expert");
        personPhoto.setImageResource(R.drawable.sai);

        Toast.makeText(getApplicationContext(), "Hello USer", Toast.LENGTH_LONG).show();

    }
    public void buttonClickHandler(View view) {
        new DownloadWebpageTask(new AsyncResult() {
            @Override
            public void onResult(JSONObject object) {
                processJson(object);
            }
        }).execute("https://spreadsheets.google.com/tq?key=1sGYusVnBg05_0l3UX_o2pRuR9PpLNK7N5c9G2zklQXk");

    }


    private void processJson(JSONObject object) {

        try {
            JSONArray rows = object.getJSONArray("rows");

            for (int r = 0; r < rows.length(); ++r) {
                JSONObject row = rows.getJSONObject(r);
                JSONArray columns = row.getJSONArray("c");

                String name = columns.getJSONObject(0).getString("v");
                int quantity = columns.getJSONObject(1).getInt("v");
                float price = columns.getJSONObject(2).getInt("v");
                String image = columns.getJSONObject(3).getString("v");
                String username = columns.getJSONObject(4).getString("v");
                Product product = new Product(name, quantity, price, image, username);
                products.add(product);
            }

            final ProductAdapter adapter = new ProductAdapter(this, R.layout.product, products);
            listview.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }






}
