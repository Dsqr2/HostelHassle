package com.example.serviceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.UpdateValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Profile extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    Button Logout;
    TextView name,email,contact;
    SharedPreferences sp;
    String emailtext;
    String nametext;
    String contacttext;
        List<List<Object>> rows2;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_profile);
        readDataFromGoogleSheet();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        Logout = findViewById(R.id.button2);
        name = findViewById(R.id.textView5);
        email = findViewById(R.id.textView6);
        contact = findViewById(R.id.textView7);

        sp = getSharedPreferences(getResources().getString(R.string.sharedpref).toString(),MODE_PRIVATE);

        nametext = sp.getString("Name",null);
        emailtext = sp.getString("Email", null);
        contacttext = sp.getString("Contact", null);
        name.setText(nametext);
        email.setText(emailtext);
        contact.setText(contacttext);

        Logout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                sp.edit().putString("islogin","false").apply();
                Intent i = new Intent(Profile.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            myClickItem(item);
            return true;
        });

    }
    public void myClickItem(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.homepage:
                Intent intent = new Intent(Profile.this, Dashboard.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;

            case R.id.lostfound:
                Intent intent2 = new Intent(Profile.this, LostFound.class);
                startActivity(intent2);
                overridePendingTransition(0, 0);
                finish();
                break;

            case R.id.profile:
                Intent intent3 = new Intent(Profile.this, Profile.class);
                startActivity(intent3);
                overridePendingTransition(0, 0);
                finish();
                break;

            case R.id.history:
                Intent intent4 = new Intent(Profile.this, History.class);
                startActivity(intent4);
                overridePendingTransition(0, 0);
                finish();
                break;
        }
    }

    private Sheets sheetsService;
    private void createSheetsService() {
        HttpTransport transport = AndroidHttp.newCompatibleTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        GoogleCredential credential = null;
        try {
            InputStream inputStream = getResources().getAssets().open("complaintboxkey.json");
            credential = GoogleCredential.fromStream(inputStream, transport, jsonFactory)
                    .createScoped(Collections.singleton(SheetsScopes.SPREADSHEETS));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheetsService = new Sheets.Builder(transport, jsonFactory, credential)
                .setApplicationName("Service App")
                .build();
    }
    private static final String SPREADSHEET_ID = "1Gw2MFn-NokjkOA_M995-mVPonGhBK1eqADsnIHl399g";
    private static final String RANGE = "Sheet1!A:D";

    private void readDataFromGoogleSheet()
    {
        String spreadsheetId = "1Gw2MFn-NokjkOA_M995-mVPonGhBK1eqADsnIHl399g";
        String range = "Sheet1!A:Z";
        String apiKey = "AIzaSyCYfMod_Z3F_hws1e---nZ8h6QGcyEd-jw";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://sheets.googleapis.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SheetsService sheetsService;
        sheetsService = retrofit.create(SheetsService.class);

        Call<ValueRange> call = sheetsService.getValues(spreadsheetId, range, apiKey);
        call.enqueue(new Callback<ValueRange>() {
            @Override
            public void onResponse(@NonNull Call<ValueRange> call, @NonNull Response<ValueRange> response)
            {
                //try {
                System.out.println(response.toString());
                ValueRange values = response.body();
                List<List<Object>> rows = values.getValues();
                System.out.println(rows);
                rows2=rows;

            }

            @Override
            public void onFailure(@NonNull Call<ValueRange> call, @NonNull Throwable t) {
                // Handle error
            }
        });
    }

}
