package com.example.cis350app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import java.util.Date;
import android.widget.Button;
import android.view.View;
import android.view.View.OnClickListener;


import com.example.cis350app.data.ReportContent.Report;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ReportDetailActivity extends AppCompatActivity {
    Report rep = null;
    private static DeleteReportTask deleteTask = null;
    private static EditReportTask editTask = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_singleton);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            Report reportInfo = (Report)
                    getIntent().getSerializableExtra("report");
            rep = reportInfo;
            arguments.putSerializable("report", reportInfo);
            ReportDetailFragment fragment = new ReportDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.report_singleton_container, fragment)
                    .commit();
        }
        ImageButton home = (ImageButton) findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(i);
            }
        });
        Button editButton = (Button) findViewById(R.id.edit_button);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReportDetailActivity.this, EditReportActivity.class));
            }
        });


    }

    public void home_button(View view) {
        startActivity(new Intent(ReportDetailActivity.this, HomeActivity.class));
    }

    public void delete_report (View v) {
        try {
            String id = rep.id;
            deleteTask = new DeleteReportTask(id);
            deleteTask.execute((Void) null);
            deleteTask = null;
            startActivity(new Intent(ReportDetailActivity.this, ReportListActivity.class));
        } catch (Exception e) {
            deleteTask = null;
        }
    }


    public static class DeleteReportTask extends AsyncTask<Void, Void, String> {

        private final String mId;

        DeleteReportTask(String id) {
            mId = id;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                System.out.println("delete url: " + "http://10.0.2.2:3000/deleteReport?id=" + mId);
                URL url = new URL("http://10.0.2.2:3000/deleteReport?id=" + mId);
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

    }

    public static class EditReportTask extends AsyncTask<Void, Void, String>{
        private final String mId;
        private final String mName;
        private final Date mDate;
        private final String mSubject;
        private final String mDescription;
        private final String mPerson;

        EditReportTask(String id, String name, Date date, String subject, String description, String person)
        {
            mId = id;
            mName = name;
            mDate = date;
            mSubject = subject;
            mDescription = description;
            mPerson = person;
        }

        @Override
        protected String doInBackground(Void...params){
            try{
                URL url = new URL("http://10.0.2.2:3000/editReport?id=" + mId + "&name=" + mName + "&Date=" + mDate
                        + "&Subject=" + mSubject + "&Description=" + mDescription + "&Person=" + mPerson);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();
                JSONObject jo = new JSONObject(msg);

                String result = jo.getString("result");
                return result;
            } catch(Exception e){
                return e.getMessage();
            }
        }

    }

}