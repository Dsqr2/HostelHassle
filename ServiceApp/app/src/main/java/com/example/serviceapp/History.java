package com.example.serviceapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class History extends AppCompatActivity {
    ListView ls;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sp;
    ArrayList<historyClass> a=new ArrayList<historyClass>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_history);

        bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.history);
        sp=getSharedPreferences(getResources().getString(R.string.sharedpref),MODE_PRIVATE);
        ls=findViewById(R.id.ls);


        readDataFromGoogleSheet();
//        a.add(new historyClass("Wifi Service","13/03/2023","Pending"));
//        a.add(new historyClass("Wifi Service","13/03/2023","Pending"));
//        a.add(new historyClass("Wifi Service","13/03/2023","Pending"));
//        a.add(new historyClass("Wifi Service","13/03/2023","Pending"));
//        a.add(new historyClass("Wifi Service","13/03/2023","Pending"));
//        a.add(new historyClass("Wifi Service","13/03/2023","Pending"));
//        a.add(new historyClass("Wifi Service","13/03/2023","Pending"));
//        a.add(new historyClass("Wifi Service","13/03/2023","Pending"));
//        a.add(new historyClass("Wifi Service","13/03/2023","Pending"));
//        a.add(new historyClass("Wifi Service","13/03/2023","Pending"));
//        a.add(new historyClass("Wifi Service","13/03/2023","Pending"));


        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            myClickItem(item);
            return true;
        });

    }

    public void myClickItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homepage:
                Intent intent = new Intent(History.this, Dashboard.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                break;

            case R.id.lostfound:
                Intent intent2 = new Intent(History.this, LostFound.class);
                startActivity(intent2);
                overridePendingTransition(0,0);
                finish();
                break;

            case R.id.profile:
                Intent intent3 = new Intent(History.this, Profile.class);
                startActivity(intent3);
                overridePendingTransition(0,0);
                finish();
                break;

            case R.id.history:
                Intent intent4 = new Intent(History.this, History.class);
                startActivity(intent4);
                overridePendingTransition(0,0);
                finish();
                break;

        }
    }
    private void readDataFromGoogleSheet()
    {
        String spreadsheetId = "1Gw2MFn-NokjkOA_M995-mVPonGhBK1eqADsnIHl399g";
        String range = "Sheet3!A:Z";
        String apiKey = "AIzaSyAtB0JJF5JEcr3gCW6W_wz2AHgtBYhGBmk";
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

                for(int i=1;rows.size()>i;i++){

                    a.add(new historyClass(rows.get(i).get(1).toString(),rows.get(i).get(2).toString(),rows.get(i).get(3).toString()));

                }
                CustomAdapter ca=new CustomAdapter(History.this,R.layout.listview,a);
                ls.setAdapter(ca);
            }

            @Override
            public void onFailure(@NonNull Call<ValueRange> call, @NonNull Throwable t) {
                Toast.makeText(History.this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                // Handle error
            }
        });


    }
}