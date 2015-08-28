package com.housing.vccalling;


import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CardViewActivity extends Activity {

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

        personName.setText("Sai Prashanth Reddy");
        personAge.setText("SEO Expert");
        personPhoto.setImageResource(R.drawable.sai);
        Toast.makeText(getApplicationContext(), "Hello USer", Toast.LENGTH_LONG).show();

    }
}