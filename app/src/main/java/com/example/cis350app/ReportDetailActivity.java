package com.example.cis350app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.util.Log;
import android.widget.ListView;

import com.example.cis350app.data.CommentContent;
import com.example.cis350app.data.ReportContent.Report;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Map;
import java.util.HashMap;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.content.Context;


public class ReportDetailActivity extends AppCompatActivity {
    public Report rep = null;
    String id;
    private static Context context;
    private static ListView comment_box = null;
    private static DeleteReportTask deleteTask = null;
    private static AddCommentTask addCommentTask = null;
    private static String commentString = null;
    private EditText newComment;
    private static CommentTask commentTask = null;
    public static List<CommentContent.Comment> ITEMS = new ArrayList<>();
    public static Map<String, CommentContent.Comment> ITEM_MAP = new HashMap<>();
    public static List<String> commentContent = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.report_singleton);

        context = getBaseContext();

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
                Intent intent = new Intent(ReportDetailActivity.this, EditReportActivity.class);
                intent.putExtra("reportID", rep.id);
                startActivity(intent);
            }
        });

        newComment = (EditText) findViewById(R.id.new_comment);

        Button comment = (Button) findViewById(R.id.submit);
        comment.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    commentString = newComment.getText().toString();
                    addCommentTask = new ReportDetailActivity.AddCommentTask(rep.id, commentString);
                    addCommentTask.execute((Void) null);
                    addCommentTask = null;
                    startActivity(new Intent(ReportDetailActivity.this, ReportListActivity.class));
                } catch (Exception e) {
                    addCommentTask = null;
                }
            }
        });

        comment_box = (ListView) findViewById(R.id.comment_box);
        try {
            commentTask = new CommentTask(rep.id);
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<>();
            commentTask.execute((Void) null);;
            List<CommentContent.Comment> comments = commentTask.get();
            //String comm = addCommentTas.get();
            Log.v("32", "com" + comments);
            ArrayAdapter contentAdapter = new ArrayAdapter(context, R.layout.activity_report_detail_comment, comments);
            Log.v("35", "" + contentAdapter);
            ListView listview = (ListView) findViewById(R.id.comment_list);
            Log.v("74", "whoop");
            listview.setAdapter(contentAdapter);
            Log.v("68", "asdfsd");
        } catch (Exception e) {
            commentTask = null;
        }




    }

    public void home_button(View view) {
        startActivity(new Intent(ReportDetailActivity.this, HomeActivity.class));
    }

    public void delete_report (View v) {
        try {
            id = rep.id;
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

    public void submit_comment(View v) {
        try {
            addCommentTask = new AddCommentTask(rep.id, commentString);
            addCommentTask.execute((Void) null);
            addCommentTask = null;
            startActivity(new Intent(ReportDetailActivity.this, ReportListActivity.class));
        } catch (Exception e) {
            addCommentTask = null;
        }
    }

    public static class AddCommentTask extends AsyncTask<Void, Void, List<CommentContent.Comment>> {

        private final String mReportId;
        private final String mContent;

        public List<CommentContent.Comment> comments = new ArrayList<>();

        AddCommentTask(String id, String content) {
            mReportId = id;
            mContent = content;
        }

        @Override
        protected List<CommentContent.Comment> doInBackground(Void... params) {
            try {
                System.out.println("comment url: " + "http://10.0.2.2:3000/addCommentAndroid?reportId=" + mReportId +
                        "&content=" + mContent);
                URL url = new URL("http://10.0.2.2:3000/addCommentAndroid?reportId=" + mReportId +
                        "&content=" + mContent);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();
                JSONObject jo = new JSONObject(msg);
                JSONArray arr = jo.getJSONArray("result");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String id = obj.getString("_id");
                    String reportId = obj.getString("reportId");
                    String content = obj.getString("content");
                    String user = obj.getString("user");
                    String role = obj.getString("role");
                    String date = obj.getString("date");
                    CommentContent.Comment c = new CommentContent.Comment(id, reportId, content, user, role, date);
                    comments.add(c);
                }

                System.out.println("adap comments" + comments);
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportDetailActivity.getActivity(), R.id.comment_list, comments);
                //create array adapter
                //ListView
                System.out.println("PRINTING ON COMMENTS FROM TASK");
                for (CommentContent.Comment c : comments) {
                    System.out.println(c.content);
                }
                return comments;
            } catch (Exception e) {
                return null;
            }
        }
    }

    public static class CommentTask extends AsyncTask<Void, Void, List<CommentContent.Comment>> {

        private final String mId;
        public List<CommentContent.Comment> comments = new ArrayList<>();


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
                for (int i = 0; i < arr.length(); i++) {

                    JSONObject obj = arr.getJSONObject(i);
                    String id = obj.getString("_id");
                    String reportId = obj.getString("reportId");
                    String content = obj.getString("content");
                    String user = obj.getString("user");
                    String role = obj.getString("role");
                    String date = obj.getString("date");
                    CommentContent.Comment c = new CommentContent.Comment(id, reportId, content, user, role, date);
                    comments.add(c);
                }
                ArrayAdapter contentAdapter = new ArrayAdapter(context, R.layout.activity_report_detail, comments);

                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportDetailActivity.getActivity(), R.id.comment_list, comments);
                //create array adapter
                //ListView
                System.out.println("BEFORE RETURNING COMMENTS");
                System.out.println("comments size: " + comments.size());
                return comments;
            } catch (Exception e) {
                return null;
            }
        }
    }
}