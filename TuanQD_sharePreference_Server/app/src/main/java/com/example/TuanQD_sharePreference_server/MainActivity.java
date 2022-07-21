package com.example.TuanQD_sharePreference_server;

import androidx.appcompat.app.AppCompatActivity;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class MainActivity extends AppCompatActivity {
    TextView txtLink;
    Button btnMakeData;
    SharedPreferences sharedPref;
    Cursor cur;
    public static final String Name = "nameKey";
    public static final String Phone = "phoneKey";
    public static final String Address = "addressKey";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtLink = findViewById(R.id.txtLink);

        // make sharePreference
        sharedPref = getSharedPreferences("APPSHARE", MODE_PRIVATE);

        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            sharedPref = EncryptedSharedPreferences.create(
                    "APPSHARE",
                    masterKeyAlias,
                    this,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Name, "Tuan");
        editor.putString(Address, "Tay Nguyen");
        editor.putString(Phone, "0988685925");
        editor.apply();

    }
}