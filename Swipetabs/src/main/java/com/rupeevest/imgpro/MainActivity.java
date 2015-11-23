package com.rupeevest.imgpro;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    ImageButton ok;
    Button camra;
    ImageView imgvu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar =(Toolbar)findViewById(R.id.app_bar);

        setSupportActionBar(toolbar);

        ok = (ImageButton)findViewById(R.id.imgimbbtn);
        imgvu =(ImageView)findViewById(R.id.imgvuimage);
        camra =(Button)findViewById(R.id.btncamra);

        //Disable "Take Picture button" if there is no camra

          if(!hasCamera())
          {
              camra.setEnabled(false);
          }



         ok.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

               //  Toast.makeText(getApplicationContext() ,"Running",Toast.LENGTH_LONG).show();

                 if(ImageEffects.getOriginalimage()!=null)
                 {
                     Intent intn = new Intent(getApplicationContext(),EditImage.class);
                     startActivity(intn);
                 }
                 else
                 {
                     Toast.makeText(getApplicationContext(),"Click an Image First",Toast.LENGTH_SHORT).show();
                 }

             }
         });

        camra.setOnClickListener( new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                launchCamera(v);
            }
        });

    }

    // Check if user has a camera
    private boolean hasCamera()
    {
        return getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    //Lauching the camera

    public void launchCamera(View view)
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //Take a picture and pass tghe results
        startActivityForResult(intent,REQUEST_IMAGE_CAPTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            imgvu.setImageBitmap(photo);
            ImageEffects.setOriginalimage(photo);

        }
    }
}
