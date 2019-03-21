package com.example.cis350app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    //to be changed
    public void reports_action(View v){
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

    public void notifications_action(View v){
        startActivity(new Intent(HomeActivity.this, NotificationListActivity.class));
    }

    public void search_for_admin_action(View v){
        startActivity(new Intent(HomeActivity.this, SearchActivity.class));
    }

    public void events_action(View v){
        startActivity(new Intent(HomeActivity.this, EventListActivity.class));
    }

    public void acct_settings_action(View v){
        startActivity(new Intent(HomeActivity.this, AccountSettingsActivity.class));
    }

    public void logout_action(View v){
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

}
