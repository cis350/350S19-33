package com.example.cis350app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import
        
public class ProfileActivity extends AppCompatActivity {

    Toolbar myToolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminprofile);

        Bundle bundle = getIntent().getExtras();
        if(bundle != null){

        }
    }
}