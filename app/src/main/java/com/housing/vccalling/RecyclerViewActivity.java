package com.housing.vccalling;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
//import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewActivity extends Activity {

    private List<Person> persons;
    private RecyclerView rv;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        rv = (RecyclerView) findViewById(R.id.rv);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        rv.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        rv.setLayoutManager(mLayoutManager);
        initializeData();

        // specify an adapter (see also next example)
        mAdapter = new RVAdapter(persons);
        rv.setAdapter(mAdapter);




    }

    private void initializeData(){
        persons = new ArrayList<>();
        persons.add(new Person("Sai Prashanth Reddy", "SEO Manager", R.drawable.sai));
        persons.add(new Person("Sravan Bonthala", "Tech Expert", R.drawable.sravan));
        persons.add(new Person("Avi Jalan", "Operations Manager", R.drawable.avi));
    }






}
