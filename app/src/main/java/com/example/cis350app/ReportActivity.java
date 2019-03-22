package com.example.cis350app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.Menu;
import android.text.TextUtils;


public class ReportActivity extends AppCompatActivity {
    private EditText name;
    private EditText date;
    private EditText subject;
    private EditText description;
    private EditText person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Initialize buttons and Edit Texts for form
        Button btnSubmit = (Button) findViewById(R.id.report_submit_button);
        //Button btnSrc = (Button) findViewById(R.id.buttonSrc);
        final EditText name = (EditText) findViewById(R.id.report_name);
        final EditText date = (EditText) findViewById(R.id.report_date);
        final EditText subject = (EditText) findViewById(R.id.report_subject);
        final EditText description = (EditText) findViewById(R.id.report_description);
        final EditText person = (EditText) findViewById(R.id.report_person);

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
            date.setError(null);
            subject.setError(null);
            description.setError(null);

            // Store values at the time of the register attempt.
            String rep_name = name.getText().toString();
            String rep_date = date.getText().toString();
            String rep_subject = subject.getText().toString();
            String rep_description = description.getText().toString();
            String rep_person = person.getText().toString();

            boolean cancel = false;
            View focusView = null;

            // Check for a date
            if (TextUtils.isEmpty(rep_date)) {
                date.setError(getString(R.string.error_field_required));
                focusView = date;
                cancel = true;
            }

            // Check for a subject
            if (!TextUtils.isEmpty(rep_subject)) {
                subject.setError(getString(R.string.error_field_required));
                focusView = subject;
                cancel = true;
            }

            if (TextUtils.isEmpty(rep_description)) {
                subject.setError(getString(R.string.error_field_required));
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


        //Listener on source button
        /*btnSrc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent j = new Intent(ReportActivity.this, MainActivity.class);
                startActivity(j);
            }
        });*/
    }
