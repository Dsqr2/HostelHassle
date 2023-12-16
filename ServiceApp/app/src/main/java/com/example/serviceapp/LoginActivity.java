package com.example.serviceapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity
{
    TextView createaccount;
    EditText email;
    EditText password;
    MaterialButton loginbtn;
    SharedPreferences sp;
    List<List<Object>> rows2;
    ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.login_activity_main);


        createaccount = findViewById(R.id.create);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        sp = getSharedPreferences(getResources().getString(R.string.sharedpref).toString(),MODE_PRIVATE);
        loginbtn = findViewById(R.id.loginbtn);
        pb = findViewById(R.id.pbar);

//        rows2.get(0).get(1);
        if(sp.getString("islogin","false").equals("true")){
            Intent intent = new Intent(LoginActivity.this, Dashboard.class);
            startActivity(intent);
            finish();
        }
        //admin and admin
        createaccount.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        loginbtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                pb.setVisibility(View.VISIBLE);
                if(!email.getText().toString().equals("") && !password.getText().toString().equals("")) {
                    readDataFromGoogleSheet();
                }
                if(email.getText().toString().equals(""))
                {
                    //error
                    email.setError("This Field is Mandatory");
                    pb.setVisibility(View.GONE);
                }
                if(password.getText().toString().equals(""))
                {
                    //error
                    password.setError("This Field is Mandatory");
                    pb.setVisibility(View.GONE);
                }


            }
        });
    }
    private void readDataFromGoogleSheet()
    {
        String spreadsheetId = "1Gw2MFn-NokjkOA_M995-mVPonGhBK1eqADsnIHl399g";
        String range = "Sheet1!A:Z";
        String apiKey = "AIzaSyCYfMod_Z3F_hws1e---nZ8h6QGcyEd-jw";
//        AIzaSyAtB0JJF5JEcr3gCW6W_wz2AHgtBYhGBmk
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
                int flag=0;
                for(int i=0; i<rows2.size(); i++)
                {
                    if(email.getText().toString().equals(rows2.get(i).get(0).toString())
                            && password.getText().toString().equals(rows2.get(i).get(3).toString()) )
                    {
                        sp.edit().putString("Email",rows.get(i).get(0).toString()).apply();
                        sp.edit().putString("Name",rows.get(i).get(1).toString()).apply();
                        sp.edit().putString("Contact",rows.get(i).get(2).toString()).apply();
                        sp.edit().putString("Password",rows.get(i).get(3).toString()).apply();
                        sp.edit().putString("islogin","true").apply();
                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, Dashboard.class);
                        startActivity(intent);
                        finish();
                        email.setText("");
                        password.setText("");
                        flag = 1;
                    }
                }
                if(flag == 0)
                {
                    Toast.makeText(LoginActivity.this, "Incorrect Credentials", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(View.GONE);
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
                Toast.makeText(LoginActivity.this, "Unable to fetch data", Toast.LENGTH_SHORT).show();
                pb.setVisibility(View.GONE);
                // Handle error
            }
        });


    }
}