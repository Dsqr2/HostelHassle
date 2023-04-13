package com.example.serviceapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Dashboard extends AppCompatActivity {

    CardView w,c,e,p,m,r,water,o;
    BottomNavigationView bottomNavigationView;
    View v,v2;
    SharedPreferences sp;
    TextView name;
    String nametext;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_dashboard);

        bottomNavigationView=findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.homepage);

        w=findViewById(R.id.cardwifi);
        c=findViewById(R.id.cardcarpenter);
        e=findViewById(R.id.cardelectrician);
        p=findViewById(R.id.cardplumber);
        m=findViewById(R.id.cardmess);
        r=findViewById(R.id.cardroomservice);
        water=findViewById(R.id.cardwater);
        o=findViewById(R.id.cardothers);
        v=findViewById(R.id.go);
        v2=findViewById(R.id.logout);
        name = findViewById(R.id.name);
        sp = getSharedPreferences(getResources().getString(R.string.sharedpref).toString(),MODE_PRIVATE);
        nametext = sp.getString("Name",null);
        name.setText(nametext);
        w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, DetailsWifi.class);
                startActivity(intent);
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, DetailsCarpenter.class);
                startActivity(intent);
            }
        });
        e.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, DetailsElectrician.class);
                startActivity(intent);
            }
        });
        p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, DetailsPlumber.class);
                startActivity(intent);
            }
        });
        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ComplaintMess.class);
                startActivity(intent);
            }
        });
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ComplaintRoomService.class);
                startActivity(intent);
            }
        });
        water.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ComplaintWater.class);
                startActivity(intent);
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, ComplaintOthers.class);
                startActivity(intent);
            }
        });
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Dashboard.this, Profile.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            myClickItem(item);
            return true;
        });
        v2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.edit().putString("islogin","false").apply();
                Intent i = new Intent(Dashboard.this,LoginActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(0, 0);
            }
        });



    }
    public void myClickItem(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homepage:
                Intent intent = new Intent(Dashboard.this, Dashboard.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                break;

            case R.id.lostfound:
                Intent intent2 = new Intent(Dashboard.this, LostFound.class);
                startActivity(intent2);
                overridePendingTransition(0,0);
                finish();
                break;

            case R.id.profile:
                Intent intent3 = new Intent(Dashboard.this, Profile.class);
                startActivity(intent3);
                overridePendingTransition(0,0);
                finish();
                break;

            case R.id.history:
                Intent intent4 = new Intent(Dashboard.this, History.class);
                startActivity(intent4);
                overridePendingTransition(0,0);
                finish();
                break;

        }


    }

}