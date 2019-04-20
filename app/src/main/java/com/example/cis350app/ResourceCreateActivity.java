package com.example.cis350app;

import android.os.AsyncTask;
import  android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;

import com.example.cis350app.data.ResourceContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import android.widget.Button;
import android.text.TextUtils;

public class ResourceCreateActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private EditText name;
    private EditText description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_resource);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button submit = (Button) findViewById(R.id.save_resource_button);
        name = (EditText) findViewById(R.id.edit_name);
        description = (EditText) findViewById(R.id.edit_description);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitResource();
            }
        });
    }

    private void submitResource() {

        name.setError(null);
        description.setError(null);

        String rep_name = name.getText().toString();
        String rep_description = description.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(rep_name)) {
            name.setError(getString(R.string.error_field_required));
            focusView = name;
            cancel = true;
        }

        if (TextUtils.isEmpty(rep_description)) {
            description.setError(getString(R.string.error_field_required));
            focusView = description;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt register and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user register attempt.
            // showProgress(true);
            //mAuthTask = new UserRegisterTask(username, password, school, age, gender);
            //mAuthTask.execute((Void) null);
        }
    }

    public class ResourceTask extends AsyncTask<Void, Void, String> {
        private final String mName;
        private final String mDescription;

        ResourceTask(String name, String description) {
            mName = name;
            mDescription = description;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(
                        "http://10.0.2.2:3000/createResource?name=" + mName +
                                "&description=" + mDescription);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();
                JSONObject jo = new JSONObject(msg);
                String result = jo.getString("result");
                return result;
            } catch (Exception e) {
                return e.getMessage();
            }
        }

        protected void onPostExecute(final String result) {
            if (result.equals("resource submitted")) {
                Intent i = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(i);
            }
        }
    }
}