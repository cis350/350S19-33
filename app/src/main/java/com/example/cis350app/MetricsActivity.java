package com.example.cis350app;

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
        getSupportActionBar().hide();

        pendingTextView = (TextView) findViewById(R.id.numPending);
        closedTextview = (TextView) findViewById(R.id.numResolved);

        mAuthTask = new MetricTask(LoginActivity.getsessionUserName());

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
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();
                JSONObject jo = new JSONObject(msg);
                JSONArray arr = jo.getJSONArray("result");
                int numPending = 0;
                int numClosed = 0;
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    Boolean closed = obj.getBoolean("closed");
                    if(closed){
                        numClosed++;
                    } else {
                        numPending++;
                    }
                }
                return numPending + " " + numClosed;
            } catch (Exception e) {
                mAuthTask = null;
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(final String result) {
            String[] results = result.split(" ");
            String numPending = results[0];
            String numClosed = results[1];
            pendingTextView.setText(numPending);
            closedTextview.setText(numClosed);
        }
    }
}
