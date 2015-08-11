package com.housing.vccalling;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;

/**
 * Created by guest on 26/5/15.
 */
public class Success extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
