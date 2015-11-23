package com.rupeevest.imgpro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.HashMap;

/**
 * Created by raHuL on 9/11/2015.
 */
public class MyFragmentPagerAdapter extends FragmentPagerAdapter
{
    final int PAGE_COUNT = 3;
     HashMap<Integer,String> tag_holder = new HashMap<Integer,String>();
     FragmentManager mfragmentmngr;

    public MyFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
        mfragmentmngr = fm;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position)
        {
            case 0:

                Effects ef = new Effects();


                  return ef;
            case 1:
                adjust af = new adjust();

                  return af;
            case 2:
                  frame fr = new frame();

                  return fr;
        }


        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        Object obj = super.instantiateItem(container, position);
        if(obj instanceof Fragment)
        {
         Fragment f = (Fragment) obj;
         String tag = f.getTag();
         tag_holder.put(position,tag);
//         Log.d("STATUS","tag tag tag-->"+tag+" position ->"+position);
        }

           return obj;
    }

    Fragment getFragment(int position)
    {
       String tag = tag_holder.get(position);
       return mfragmentmngr.findFragmentByTag(tag);
    }
}
