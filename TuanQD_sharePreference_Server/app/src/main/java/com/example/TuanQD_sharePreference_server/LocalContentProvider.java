package com.example.TuanQD_sharePreference_server;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.security.keystore.KeyGenParameterSpec;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.HashMap;

public class LocalContentProvider extends ContentProvider {

    static final String PROVIDER_NAME = "com.example.TuanQD_sharePreference_server.LocalContentProvider";
    static final String URL = "content://" + PROVIDER_NAME + "/users";
    static final Uri CONTENT_URI = Uri.parse(URL);
    static final String _ID = "_id";
    static final String NAME = "nameKey";
    static final String ADDRESS = "addressKey";
    static final String PHONENUMBER = "phoneKey";
    private static final String TAG = "LocalContentProvider";

    private static HashMap<String, String> USERS_PROJECTION_MAP;
    static final int USERS = 1;
    static final int USER_ID = 2;

    String name,address,phone;

//    static final UriMatcher uriMatcher;

//    static {
//        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
//        uriMatcher.addURI(PROVIDER_NAME, "users", USERS);// CONTENT_URI== users query.
//        uriMatcher.addURI(PROVIDER_NAME, "users/#", USER_ID);
//    }

    private SQLiteDatabase db;
    static final String DATABASE_NAME = "Customer";
    static final String USERS_TABLE_NAME = "users";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_DB_TABLE =
            " CREATE TABLE " + USERS_TABLE_NAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " name TEXT NOT NULL, " +
                    " address TEXT NOT NULL," +
                    "phone_number TEXT NOT NULL);";

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_DB_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {

        KeyGenParameterSpec keyGenParameterSpec = MasterKeys.AES256_GCM_SPEC;
        String masterKeys;
        try {
            masterKeys = MasterKeys.getOrCreate(keyGenParameterSpec);
            SharedPreferences sharedPreferences = EncryptedSharedPreferences.create(
                    "APPSHARE",
                    masterKeys,
                    getContext(),
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
                    name = sharedPreferences.getString(NAME, "");
                    phone = sharedPreferences.getString(PHONENUMBER, "");
                    address = sharedPreferences.getString(ADDRESS, "");
            Log.i(TAG, "onCreate: "+name+" "+phone+" "+address);

        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection,
                        @Nullable String selection, @Nullable String[] selectionArgs,
                        @Nullable String sortOrder) {
        Cursor cursor = initCursor();
        Log.e(TAG, "query: "+cursor);
        return cursor;
    }

    public Cursor initCursor(){
        String[] columns = new String[]{"name", "phone", "address"};
        MatrixCursor matrixCursor = new MatrixCursor(columns);

        matrixCursor.addRow(new Object[]{name,phone,address});
        Log.i(TAG, "initCursor: "+name+" "+phone+" "+address);
        return matrixCursor;


    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        long rowID = db.insert(USERS_TABLE_NAME, "", values);
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
