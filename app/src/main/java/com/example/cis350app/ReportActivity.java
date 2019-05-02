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
import android.widget.Toast;

import java.net.URL;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.example.cis350app.data.ReportContent;
import java.util.Date;


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
        name = (EditText) findViewById(R.id.report_name);
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
        name.setError(null);
        subject.setError(null);
        description.setError(null);
        person.setError(null);

        // Store values at the time of the register attempt.
        String rep_name = name.getText().toString();
        Date daDate = new Date();
        Date rep_date = daDate;
        String rep_subject = subject.getText().toString();
        String rep_description = description.getText().toString();
        String rep_person = person.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(rep_name)){
            name.setError("This field is required");
            focusView = name;
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
            CreateReportTask task = new CreateReportTask(rep_name, daDate, rep_subject, rep_description, rep_person);
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
        private final String mName;
        private final Date mDate;
        private final String mSubject;
        private final String mDescription;
        private final String mPerson;

        CreateReportTask(String name, Date date, String subject, String description, String person) {
            mName = name;
            mDate = date;
            mSubject = subject;
            mDescription = description;
            mPerson = person;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                String mUsername = LoginActivity.getsessionUserName();
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

        @Override
        protected void onPostExecute(final String result) {
            if (result.contains("report submitted")) {
                Toast myToast =Toast.makeText(getApplicationContext(), "You successfully created a report!",
                        Toast.LENGTH_LONG);
                myToast.setMargin(50,50);
                myToast.show();
            } else {
                Toast myToast =Toast.makeText(getApplicationContext(), "There was a problem in creating a report.",
                        Toast.LENGTH_LONG);
                myToast.setMargin(50,50);
                myToast.show();
            }
        }

    }

    public void home_button(View view) {
        startActivity(new Intent(ReportActivity.this, HomeActivity.class));
    }

}