package com.rupeevest.imgpro;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.Window;

import java.util.HashMap;
import java.util.List;

public class EditImage extends AppCompatActivity {

    ActionBar actionbar;
    ViewPager viewpager;
    Toolbar toolbar=null;
    String tab_1,tab_2;
    static int counter=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


       Bitmap bmp = ((BitmapDrawable) getApplicationContext().getResources().getDrawable(R.drawable.champa)).getBitmap();
        ImageEffects.setOriginalimage(bmp);


//  Log.d("IMAGE", "IMAGE HEIGHT--" + ImageEffects.getOriginalimage().getHeight() + "Image WIdth" + ImageEffects.getOriginalimage().getWidth());


        setContentView(R.layout.activity_edit_image);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);

        tabLayout.addTab(tabLayout.newTab().setText("Top Voted"));
        tabLayout.addTab(tabLayout.newTab().setText("Adjust"));
        tabLayout.addTab(tabLayout.newTab().setText("Frames"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.viewpgid);
        final MyFragmentPagerAdapter fragmntadaptr = new MyFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(fragmntadaptr);



        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                //View parent = v.getRootView();
                //ViewPager viewPager = (ViewPager) parent.findViewById(R.id.viewpgid);

               // if(counter>3)

                   //Fragment frgmnt = MyFragmentPagerAdapter.getFragment(1);

                  // Fragment frgmnt = MyFragmentPagerAdapter.getFragment(1);
                  // MyFragmentPagerAdapter.tag_holder.size();

                //  Fragment fragment = ((MyFragmentPagerAdapter)viewPager.getAdapter()).getFragment(tab.getPosition());

                //    if (tab.getPosition() == 1 && fragment != null) {
                //        fragment.onResume();
                //    }


                //counter++;

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab)
            {
                tab_1 = tab.getText().toString();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setCurrentItem(1); // making tab as default tab....
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }




        return super.onOptionsItemSelected(item);
    }


}

