package com.housing.vccalling;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by guest on 26/5/15.
 */
public class Register extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    public void onClickAddName(View view) {
        // Add a new student record
        ContentValues values = new ContentValues();

        values.put(EmployeeProvider.NAME,
                ((EditText)findViewById(R.id.txtName)).getText().toString());

        values.put(EmployeeProvider.EMAIL,
                ((EditText)findViewById(R.id.txtEmail)).getText().toString());

        values.put(EmployeeProvider.EMPLOYEE_ID,
                ((EditText)findViewById(R.id.txtEmployee)).getText().toString());

        values.put(EmployeeProvider.PHONE_NUMBER,
                ((EditText)findViewById(R.id.txtMobile)).getText().toString());

        Uri uri = getContentResolver().insert(
                EmployeeProvider.CONTENT_URI, values);

        Toast.makeText(getBaseContext(),
                uri.toString(), Toast.LENGTH_LONG).show();
        Intent i = new Intent(this, Success.class);
        i.putExtra("Login", "Yes");

// Starts TargetActivity
        startActivity(i);


    }

    public void onClickRetrieveEmployees(View view) {
        // Retrieve student records
        String URL = "content://com.housing.provider.Company/employees";
        Uri employees = Uri.parse(URL);
        Cursor c = getContentResolver().query(employees, null, null, null, "name");
        if (c.moveToFirst()) {
            do{
                Toast.makeText(this,
                        c.getString(c.getColumnIndex(EmployeeProvider._ID)) +
                                ", " +  c.getString(c.getColumnIndex( EmployeeProvider.NAME)) +
                                ", " + c.getString(c.getColumnIndex( EmployeeProvider.EMAIL)) +
                                ", " +  c.getString(c.getColumnIndex( EmployeeProvider.EMPLOYEE_ID)) +
                                ", " +  c.getString(c.getColumnIndex( EmployeeProvider.PHONE_NUMBER)),
                        Toast.LENGTH_SHORT).show();
            } while (c.moveToNext());
        }
    }
}