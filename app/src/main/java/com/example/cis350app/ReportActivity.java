package com.example.cis350app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.Menu;
import android.text.TextUtils;

import java.net.URL;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.example.cis350app.data.ReportContent;



public class ReportActivity extends AppCompatActivity {
    private EditText username;
    private EditText name;
    private EditText date;
    private EditText subject;
    private EditText description;
    private EditText person;
    private String editItems = "";
    private CreateReportTask task = null;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize buttons and Edit Texts for form
        Button btnSubmit = (Button) findViewById(R.id.report_submit_button);
        //Button btnSrc = (Button) findViewById(R.id.buttonSrc);
        username = (EditText) findViewById(R.id.report_username);
        name = (EditText) findViewById(R.id.report_name);
        date = (EditText) findViewById(R.id.report_date);
        subject = (EditText) findViewById(R.id.report_subject);
        description = (EditText) findViewById(R.id.report_description);
        person = (EditText) findViewById(R.id.report_person);

        //Listener on Submit button
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReport();
            }
        });
    }



    private void submitReport() {

        // Reset errors.
        //name.setError(null);
        username.setError(null);
        name.setError(null);
        date.setError(null);
        subject.setError(null);
        description.setError(null);
        person.setError(null);

        // Store values at the time of the register attempt.
        String rep_username = username.getText().toString();
        String rep_name = name.getText().toString();
        String rep_date = date.getText().toString();
        String rep_subject = subject.getText().toString();
        String rep_description = description.getText().toString();
        String rep_person = person.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if(TextUtils.isEmpty(rep_username)){
            username.setError("This field is required");
            focusView = username;
            cancel = true;

        }

        if(TextUtils.isEmpty(rep_name)){
            name.setError("This field is required");
            focusView = name;
            cancel = true;

        }

        // Check for a date
        if (TextUtils.isEmpty(rep_date)) {
            date.setError(getString(R.string.error_field_required));
            focusView = date;
            cancel = true;
        }

        // Check for a subject
        if (TextUtils.isEmpty(rep_subject)) {
            subject.setError(getString(R.string.error_field_required));
            focusView = subject;
            cancel = true;
        }

        if (TextUtils.isEmpty(rep_description)) {
            description.setError(getString(R.string.error_field_required));
            focusView = description;
            cancel = true;
        }
        if(TextUtils.isEmpty(rep_person)){
            person.setError("This field is required");
            focusView = person;
            cancel = true;
        } else {
            CreateReportTask task = new CreateReportTask(rep_username, rep_name, rep_date, rep_subject, rep_description, rep_person);
            task.execute((Void) null);
            task = null;
            // Show a progress spinner, and kick off a background task to
            // perform the user register attempt.
            // showProgress(true);
            //mAuthTask = new UserRegisterTask(username, password, school, age, gender);
            //mAuthTask.execute((Void) null);
        }
    }


    //Listener on source button
        /*btnSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(ReportActivity.this, MainActivity.class);
                startActivity(j);
            }
        });*/

    //create report
    public class CreateReportTask extends AsyncTask<Void, Void, String> {
        private final String mUsername;
        private final String mName;
        private final String mDate;
        private final String mSubject;
        private final String mDescription;
        private final String mPerson;

        CreateReportTask(String username, String name, String date, String subject, String description, String person) {
            mUsername = username;
            mName = name;
            mDate = date;
            mSubject = subject;
            mDescription = description;
            mPerson = person;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(
                        "http://10.0.2.2:3000/saveStudentReport?username=" + mUsername + "&name=" + mName +
                                "&date=" + mDate + "&subject=" + mSubject + "&description=" + mDescription +
                                "&person=" + mPerson);
                System.out.println(url);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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


        /*@Override
        protected void onPostExecute(final String result) {
            showProgress(false);

            if (result.equals("signed up")) {
                Intent i = new Intent(getBaseContext(), ReportListActivity.class);
                startActivity(i);
            } else if (result.equals("username taken")) {
                mUsernameView.setError(getString(R.string.error_username_taken));
                mUsernameView.requestFocus();
            } else {
//                mUsernameView.setError(getString(R.string.error_register));
                mUsernameView.setError(result);
                mUsernameView.requestFocus();
            }
        }*/

    }

}