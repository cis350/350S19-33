package com.example.cis350app;

import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import com.example.cis350app.data.ReportContent;
import com.example.cis350app.data.CommentContent;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import android.view.View;
import android.view.View.OnClickListener;


import android.preference.PreferenceManager;

public class ReportDetailActivity extends AppCompatActivity {
    private static DeleteReportTask deleteTask = null;
    private static String id;
    private static String commentString = null;
    private static CommentTask commentTask = null;
    public static List<CommentContent.Comment> ITEMS = new ArrayList<>();
    public static Map<String, CommentContent.Comment> ITEM_MAP = new HashMap<String, CommentContent.Comment>();


    /**
     * An activity representing a single Report detail screen. This
     * activity is only used on narrow width devices. On tablet-size devices,
     * item details are presented side-by-side with a list of items
     * in a {@link ReportListActivity}.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);


        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        Button btnSubmitComment = (Button) findViewById(R.id.submit_comment_button);
        final EditText newComment = (EditText) findViewById(R.id.new_comment);

        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    commentString = newComment.getText().toString();
                    commentTask = new CommentTask(id);
                    commentTask.execute((Void) null);
                    commentTask = null;
                    startActivity(new Intent(ReportDetailActivity.this, ReportListActivity.class));
                } catch (Exception e) {
                    commentTask = null;
                }
            }
        });

        Button btnEdit = (Button) findViewById(R.id.edit_submit_button);
        final EditText mEdit = (EditText) findViewById(R.id.report_detail);


        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {

            id = getIntent().getStringExtra(ReportDetailFragment.ARG_ITEM_ID);
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ReportDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ReportDetailFragment.ARG_ITEM_ID));
            ReportDetailFragment fragment = new ReportDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.report_detail_container, fragment)
                    .commit();
        }

        try {
            //String username = LoginActivity.getsessionUserName();
            commentTask = new CommentTask(id);
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<String, CommentContent.Comment>();
            commentTask.execute((Void) null);;
            List<CommentContent.Comment> comments = commentTask.get();
            for (CommentContent.Comment c : comments) {
                System.out.println(c);
                ITEMS.add(c);
                ITEM_MAP.put(c.id, c);
            }
            commentTask = null;
        } catch (Exception e) {
            commentTask = null;
        }
        Button btnDelete = (Button) findViewById(R.id.delete_submit_button);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {

                    deleteTask = new ReportDetailActivity.DeleteReportTask(id);
                    deleteTask.execute((Void) null);
                    deleteTask = null;
                    startActivity(new Intent(ReportDetailActivity.this, ReportListActivity.class));
                } catch (Exception e) {
                    deleteTask = null;
                }
            }
        });




    }

    public void SavePreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("state", findViewById(R.id.edit_submit_button).isEnabled());
    }

    public void LoadPreferences() {
        SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
        Boolean state = sharedPreferences.getBoolean("state", false);
        findViewById(R.id.edit_submit_button).setEnabled(state);
    }

    @Override
    public void onBackPressed() {
        SavePreferences();
        super.onBackPressed();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            NavUtils.navigateUpTo(this, new Intent(this, ReportListActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DeleteReportTask extends AsyncTask<Void, Void, String> {

        private final String mId;

        DeleteReportTask(String id) {
            mId = id;
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
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

    public static class CommentTask extends AsyncTask<Void, Void, List<CommentContent.Comment>> {

        private final String mId;

        CommentTask(String id) {
            mId = id;
        }

        @Override
        protected List<CommentContent.Comment> doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/getComment?reportId=" + mId);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();

                JSONObject jo = new JSONObject(msg);
                JSONArray arr = jo.getJSONArray("result");
                List<CommentContent.Comment> comments = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String id = obj.getString("id");
                    String reportId = obj.getString("reportId");
                    String content = obj.getString("content");
                    String user = obj.getString("user");
                    String role = obj.getString("role");
                    String date = obj.getString("date");
                    CommentContent.Comment c = new CommentContent.Comment(id, reportId, content, user, role, date);
                    comments.add(c);
                }
                return comments;
            } catch (Exception e) {
                return null;
            }
        }

    }

    /*public static class CommentTask extends AsyncTask<Void, Void, List<ReportContent.Report>> {
        @Override
        protected List<ReportContent.Report> doInBackground(Void... params) {
            try {

                URL url = new URL("http://10.0.2.2:3000/addComment?id=" +
                        id + "&comment=" + commentString);
                System.out.println(url);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();
                JSONObject jo = new JSONObject(msg);
                JSONArray arr = jo.getJSONArray("result");
                List<ReportContent.Report> reports = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String id = obj.getString("id");
                    String username = obj.getString("studentUsername");
                    String name = obj.getString("studentName");
                    String date = obj.getString("date");
                    String subject = obj.getString("subject");
                    String description = obj.getString("reportDescription");
                    //System.out.println("description" + description);
                    String person = obj.getString("reportForWhom");
                   /* JSONArray commentsJSON = obj.getJSONArray("comments");
                    List<String> comments = new ArrayList<String>(){};
                    for (int j = 0; j < commentsJSON.length(); j++) {
                        comments.add(commentsJSON.optString(j));
                    }
                    ReportContent.Report r =
                            new ReportContent.Report(id, name, username, date, subject, description, person);
                    reports.add(r);
                }

                return reports;

            } catch (Exception e) {
                return null;
            }
        }
    }*/

}