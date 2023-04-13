package com.example.serviceapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

public class LostFound extends AppCompatActivity {

    EditText roomno, item, description;
    TextView t;
    Button submit;
    BottomNavigationView bottomNavigationView;
    SharedPreferences sp;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lostfound);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.lostfound);

        roomno=findViewById(R.id.editText);
        description=findViewById(R.id.editText2);
        item=findViewById(R.id.editText3);
        t=findViewById(R.id.textView);
        submit=findViewById(R.id.button);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sp = getSharedPreferences(getResources().getString(R.string.sharedpref).toString(),MODE_PRIVATE);
        submit.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SuspiciousIndentation")
            @Override
            public void onClick(View v) {
                String room = roomno.getText().toString();
                String des = description.getText().toString();
                String itm = item.getText().toString();

                if(room.isEmpty()) {
                    roomno.setError("This Field is Mandatory");
                }
                if(des.isEmpty()) {
                    description.setError("This Field is Mandatory");
                }
                if(itm.isEmpty()) {
                    item.setError("This Field is Mandatory");
                }
                else{
                    createSheetsService();
                    ValueRange body = new ValueRange()
                            .setValues(Arrays.asList(
                                    Arrays.asList(sp.getString("Email",null),"",roomno.getText().toString(),"","Lost","",item.getText().toString(),description.getText().toString())
                            ));
                    appendDataToSheet(body);
                    Toast.makeText(LostFound.this, "Service Registered", Toast.LENGTH_SHORT).show();
                    roomno.setText("");
                    description.setText("");
                    item.setText("");
                }
            }
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            myClickItem(item);
            return true;
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent =new Intent(LostFound.this,History.class);
                startActivity(intent);
            }
        });

    }
    public void myClickItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homepage:
                Intent intent = new Intent(LostFound.this, Dashboard.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                break;

            case R.id.lostfound:
                Intent intent2 = new Intent(LostFound.this, LostFound.class);
                startActivity(intent2);
                overridePendingTransition(0, 0);
                finish();
                break;

            case R.id.profile:
                Intent intent3 = new Intent(LostFound.this, Profile.class);
                startActivity(intent3);
                overridePendingTransition(0, 0);
                finish();
                break;

            case R.id.history:
                Intent intent4 = new Intent(LostFound.this, History.class);
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
    private static final String RANGE = "Sheet2!A:Z";

    private void appendDataToSheet(ValueRange body) {
        try {
            AppendValuesResponse result = sheetsService.spreadsheets().values()
                    .append(SPREADSHEET_ID, RANGE, body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
            Log.d(TAG, "Append result: " + result);
            Toast.makeText(this, "Request Noted", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Unable to send data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}