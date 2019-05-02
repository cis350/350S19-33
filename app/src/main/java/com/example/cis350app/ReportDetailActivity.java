package com.example.cis350app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.example.cis350app.data.ReportContent.Report;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ReportDetailActivity extends AppCompatActivity {
    Report rep = null;
    private static DeleteReportTask deleteTask = null;


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
            System.out.println("reportinto: " + reportInfo.toString());
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

    }

    public void home_button(View view) {
        startActivity(new Intent(ReportDetailActivity.this, HomeActivity.class));
    }

    public void delete_report (View v) {
        try {
            String id = rep.id;
            deleteTask = new ReportDetailActivity.DeleteReportTask(id);
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

}