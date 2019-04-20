package com.example.cis350app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.os.AsyncTask;
import android.os.Bundle;
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
import android.widget.Toast;

import com.example.cis350app.data.NotificationContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MetricsActivity extends AppCompatActivity {
    private MetricTask mAuthTask = null;
    private TextView pendingTextView = null;
    private TextView closedTextview = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metrics);

        pendingTextView = (TextView) findViewById(R.id.numPending);
        closedTextview = (TextView) findViewById(R.id.numResolved);

        mAuthTask = new MetricTask(LoginActivity.getsessionUserName());
        mAuthTask.execute((Void) null);
    }

    public class MetricTask extends AsyncTask<Void, Void, String> {

        private final String mUsername;

        MetricTask(String username) {
            mUsername = username;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL(
                        "http://10.0.2.2:3000/getMetrics?username=" + mUsername);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();
                JSONObject jo = new JSONObject(msg);
                System.out.println("jo : " + jo);
                //if it is empty then set as 0
                String returned = jo.getString("result");
                if(!returned.equals("")) {
                    if (returned == null || returned.equals("null") ||
                            returned.equals("error")) {
                        pendingTextView.setText("Error");
                        closedTextview.setText("Error");
                    } else if (returned.equals("0")){
                        pendingTextView.setText("0");
                        closedTextview.setText("0");
                    } else {
                        JSONObject metricObj = new JSONObject(returned);
                        pendingTextView.setText(metricObj.getString("numPending"));
                        closedTextview.setText(metricObj.getString("numClosed"));
                    }
                } else {
                    pendingTextView.setText("0");
                    closedTextview.setText("0");
                }
                return "done";
            } catch (Exception e) {
                mAuthTask = null;
                return "exception";
            }
        }
    }
}

