package com.example.TuanQD_sharePreference_client;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentProviderClient;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    Cursor cursor;
    TextView txtName, txtPhone, txtAddress;
    String name, phone, address;

    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtAddress = findViewById(R.id.txtAddress);
        txtName = findViewById(R.id.txtName);
        txtPhone = findViewById(R.id.txtPhone);
        Uri uri = Uri.parse("content://com.example.tuanqd_sharepreference_server.localcontentprovider");
        cursor = getContentResolver().query(uri, null, null, null, null);

        while (cursor.moveToNext()) {
            name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            phone = cursor.getString(cursor.getColumnIndexOrThrow("phone"));
            address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
            Log.i(TAG, "onCreate: " + name + " " + phone + " " + address + " ");
        }
        txtName.setText("Name: " + name);
        txtPhone.setText("Phone Number :" + phone);
        txtAddress.setText("Address: " + address);


    }
}