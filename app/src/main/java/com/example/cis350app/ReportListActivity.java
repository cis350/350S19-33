package com.example.cis350app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ArrayAdapter;


import com.example.cis350app.data.ReportContent.Report;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * An activity representing a list of Notifications. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link NotificationDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */

public class ReportListActivity extends AppCompatActivity {
    private static ReportTask reportTask = null;

    public static List<String> ITEMS = new ArrayList<>();
    public static Map<String, Report> ITEM_MAP = new HashMap<String, Report>();
    ListView report_list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);

        report_list = (ListView) findViewById(R.id.report_list);

        try {
            String username = LoginActivity.getsessionUserName();
            reportTask = new ReportTask(username);
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<String, Report>();
            reportTask.execute((Void) null);
            List<Report> reports = reportTask.get();
            for (Report r : reports) {
                ITEMS.add(r.subject);
                ITEM_MAP.put(r.subject, r);
            }
            reportTask = null;
        } catch (Exception e) {
            reportTask = null;
        }

        adapter = new ArrayAdapter<String>(
                ReportListActivity.this,
                android.R.layout.simple_list_item_1, //fix this
                ITEMS
        );

        //put listeners on the admin items in the listviews
        report_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ReportListActivity.this, ReportDetailActivity.class);
                String resourceSelected = report_list.getItemAtPosition(position).toString();
                System.out.println("resourceselected: " + resourceSelected);
                Report r = getReport(resourceSelected);
                System.out.println("report: " + r.toString());
                intent.putExtra("report", r);
                startActivity(intent);
            }
        });

        report_list.setAdapter(adapter);

    }

    public static String getReportID() {
        return reportTask.getID();

    }



    public static class ReportTask extends AsyncTask<Void, Void, List<Report>> {

        private final String mUsername;
        public String id;

        ReportTask(String username) {
            mUsername = username;
        }
        @Override
        protected List<Report> doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/getReport?username=" + mUsername);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();

                JSONObject jo = new JSONObject(msg);
                JSONArray arr = jo.getJSONArray("result");
                List<Report> reports = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                     String id = obj.getString("id");
                    String username = obj.getString("studentUsername");
                    String name = obj.getString("studentName");
                    String date = obj.getString("date");
                    String subject = obj.getString("subject");
                    String description = obj.getString("reportDescription");
                    System.out.println("description" + description);
                    String person = obj.getString("reportForWhom");
                    Boolean closed = obj.getBoolean("closed");
                   // JSONArray commentsJSON = obj.getJSONArray("comments");
                    /*System.out.println("comm JSON" + commentsJSON);
                    List<String> comments = new ArrayList<String>(){};
                    for (int j = 0; j < commentsJSON.length(); j++) {
                        comments.add(commentsJSON.optString(j));
                    }*/
                    Report r = new Report(id, username, name, date, subject, description, person, closed);
                    System.out.println(r.username);
                    System.out.println(r.description);
                    reports.add(r);
                }
                return reports;
            } catch (Exception e) {
                return null;
            }

        }
        public String getID(){
            return id;
        }

    }

    public static Report getReport(String name){
        return ITEM_MAP.get(name);
    }
    public static Report getReportID(String id){return ITEM_MAP.get(id);}

    public void home_button(View view) {
        startActivity(new Intent(ReportListActivity.this, HomeActivity.class));
    }
}

