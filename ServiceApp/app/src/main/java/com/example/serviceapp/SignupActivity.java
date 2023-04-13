package com.example.serviceapp;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
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
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignupActivity extends AppCompatActivity {

    EditText name;
    EditText email;
    EditText contact;
    EditText password;
    EditText cpassword;
    MaterialButton signupbtn;
    TextView loginbtn;
    List<List<Object>> rows2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        contact = findViewById(R.id.cno);
        password = findViewById(R.id.password);
        cpassword = findViewById(R.id.cpassword);
        signupbtn = findViewById(R.id.signupbtn);
        loginbtn = findViewById(R.id.loginbtn);
        createSheetsService();

        signupbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (name.getText().toString().equals("")) {
                    name.setError("This Field is Mandatory");
                }
                if (email.getText().toString().equals("")) {
                    email.setError("This Field is Mandatory");
                }
                if (contact.getText().toString().equals("")) {
                    contact.setError("This Field is Mandatory");
                }
                if (password.getText().toString().equals("")) {
                    password.setError("This Field is Mandatory");
                }
                if (cpassword.getText().toString().equals("")) {
                    cpassword.setError("This Field is Mandatory");
                }

                else if (password.getText().toString().equals(cpassword.getText().toString()))
                {
                    readDataFromGoogleSheet();
//                    Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
//                    startActivity(intent);
                }
                else
                {
                    cpassword.setError("Check Password");
                }

                if(password.getText().toString().equals(cpassword.getText().toString())) {
                    ValueRange body = new ValueRange()
                            .setValues(Arrays.asList(
                                    Arrays.asList(email.getText().toString(), name.getText().toString(), contact.getText().toString(), password.getText().toString())
                            ));
                    appendDataToSheet(body);
                }
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(intent1);
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
    private static final String RANGE = "Sheet1!A:Z";

    private void appendDataToSheet(ValueRange body) {
        try {
            AppendValuesResponse result = sheetsService.spreadsheets().values()
                    .append(SPREADSHEET_ID, RANGE, body)
                    .setValueInputOption("USER_ENTERED")
                    .execute();
            Log.d(TAG, "Append result: " + result);

        } catch (IOException e) {
            Toast.makeText(this, "Unable to send data", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void readDataFromGoogleSheet()
    {
        String spreadsheetId = "1Gw2MFn-NokjkOA_M995-mVPonGhBK1eqADsnIHl399g";
        String range = "Sheet1!A:Z";
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
                rows2=rows;

                if (password.getText().toString().equals(cpassword.getText().toString()))
                {
                    for(int i=0; i<rows2.size(); i++)
                    {
                        if(email.getText().toString().equals(rows2.get(i).get(0).toString())
                                || contact.getText().toString().equals(rows2.get(i).get(2).toString()) )
                        {
                            Toast.makeText(SignupActivity.this, "Existing User", Toast.LENGTH_SHORT).show();
                            break;
                        }

                    }

                }
                else
                {
                    Toast.makeText(SignupActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                }



                // Process the rows here
                //System.out.println(rows.toString());
                // Toast.makeText(MainActivity.this, rows.get(1).get(0).toString(), Toast.LENGTH_SHORT).show();
                //}
//                catch (AssertionError a){
//                    System.out.println(a.getMessage());
//                    Toast.makeText(MainActivity.this, a.getMessage(), Toast.LENGTH_SHORT).show();
//                }
            }

            @Override
            public void onFailure(@NonNull Call<ValueRange> call, @NonNull Throwable t) {
                Toast.makeText(SignupActivity.this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                // Handle error
            }
        });


    }

}