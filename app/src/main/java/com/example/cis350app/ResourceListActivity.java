package com.example.cis350app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ResourceListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_list);
    }

    public void create_resource(View view) {
        startActivity(new Intent(ResourceActivity.this, ResourceCreateActivity.class));
    }
}