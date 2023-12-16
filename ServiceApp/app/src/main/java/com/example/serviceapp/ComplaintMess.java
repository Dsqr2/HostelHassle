package com.example.serviceapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class ComplaintMess extends AppCompatActivity {

    View b;
    EditText problem;
    TextView t;
    Button submit;
    ProgressBar pb;
    SharedPreferences sp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_complaintmess);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        b=findViewById(R.id.back);
        problem=findViewById(R.id.editText2);
        t=findViewById(R.id.textView);
        pb = findViewById(R.id.pbar);
        submit=findViewById(R.id.button);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sp = getSharedPreferences(getResources().getString(R.string.sharedpref).toString(),MODE_PRIVATE);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ComplaintMess.this, Dashboard.class);
//                startActivity(intent);
                finish();
            }
        });

        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComplaintMess.this,History.class);
                startActivity(intent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pb.setVisibility(View.VISIBLE);
                String prblm = problem.getText().toString();

                if(prblm.isEmpty()) {
                    problem.setError("This Field is Mandatory");
                    pb.setVisibility(View.GONE);
                }

                else{
                    createSheetsService();
                    ValueRange body = new ValueRange()
                            .setValues(Arrays.asList(
                                    Arrays.asList(sp.getString("Email",null),problem.getText().toString(),"","","Mess Complaint")
                            ));
                    appendDataToSheet(body);
                    Toast.makeText(ComplaintMess.this, "Service Registered", Toast.LENGTH_SHORT).show();
                    problem.setText("");

                    createSheetsService();
                    ValueRange body2 = new ValueRange()
                            .setValues(Arrays.asList(
                                    Arrays.asList(sp.getString("Email",null),"Mess",currentDate.toString(),"Pending")
                            ));
                    appendDataToSheet2(body2);
                }
            }
        });
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
//            Toast.makeText(this, "Request Noted", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Unable to send data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            pb.setVisibility(View.GONE);
        }
    }
    private void appendDataToSheet2(ValueRange body) {
        final String RANGE2 = "Sheet3!A:Z";
        try {
            AppendValuesResponse result = sheetsService.spreadsheets().values()
                    .append(SPREADSHEET_ID, RANGE2, body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
            Log.d(TAG, "Append result: " + result);
            Toast.makeText(ComplaintMess.this, "Service Registered", Toast.LENGTH_SHORT).show();
            finish();

        } catch (IOException e) {
            e.printStackTrace();
            pb.setVisibility(View.GONE);
        }
    }
}