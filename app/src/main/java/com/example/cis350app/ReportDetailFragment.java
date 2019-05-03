package com.example.cis350app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import android.os.AsyncTask;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.cis350app.data.ReportContent.Report;
import com.example.cis350app.data.CommentContent;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ReportDetailFragment extends Fragment {
    public static ArrayList<String> ITEMS = new ArrayList<>();
    public static Map<String, Report> ITEM_MAP = new HashMap<>();
    private static String commentString = null;
    private static DeleteReportTask deleteTask = null;
    private static AddCommentTask addCommentTask = null;

    private Report mItem;

    public ReportDetailFragment() {
    }




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.report_singleton);

        // Load the dummy content specified by the fragment
        // arguments. In a real-world scenario, use a Loader
        // to load content from a content provider.
        mItem = (Report) getArguments().getSerializable("report");
        Activity activity = this.getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        System.out.println("ON CREATE VIEW!!!!");

        View rootView = inflater.inflate(R.layout.report_singleton, container, false);

        Activity activity = this.getActivity();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.subject)).setText("Subject: " + mItem.subject);
            ((TextView) rootView.findViewById(R.id.closed)).setText("Closed: " + Boolean.toString(mItem.closed));
            ((TextView) rootView.findViewById(R.id.student)).setText("Student Name: " + mItem.name);
            ((TextView) rootView.findViewById(R.id.admin)).setText("Admin Email: " + mItem.admin);

            Date mydate = new Date(mItem.date);
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            try {
                mydate = format.parse(mItem.date);
            } catch (Exception e){

            }

            ((TextView) rootView.findViewById(R.id.date)).setText("Date: " + mydate.toString());
            ((TextView) rootView.findViewById(R.id.desc)).setText("Description: " + mItem.description);
            ((ImageButton) rootView.findViewById(R.id.home_button)).
                    setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(getContext(), HomeActivity.class);
                            startActivity(i);
                        }
                    });
        }

        //submit comment button

        Button btnSubmitComment = (Button) rootView.findViewById(R.id.submit);
        final EditText newComment = (EditText) rootView.findViewById(R.id.new_comment);

        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    commentString = newComment.getText().toString();
                    addCommentTask = new AddCommentTask(mItem.id, commentString);
                    addCommentTask.execute((Void) null);
                    addCommentTask = null;
                    //comments.add()
                    //create addCommentTask()
                    //create set On Click Listener and then add that comment to the comments list

                    startActivity(new Intent(getContext(), ReportListActivity.class));
                } catch (Exception e) {
                    addCommentTask = null;
                }
            }
        });


        //edit button
        Button btnEdit = (Button) rootView.findViewById(R.id.edit_submit_button);
        final EditText mEdit = (EditText) rootView.findViewById(R.id.report_detail);

        //delete button
        Button btnDelete = (Button) rootView.findViewById(R.id.delete_button);
        System.out.println("delete button null: " + btnDelete == null);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    System.out.println("ONCLICK DELETE");
                    String id = mItem.id;
                    deleteTask = new ReportDetailFragment.DeleteReportTask(id);
                    deleteTask.execute((Void) null);
                    deleteTask = null;
                    startActivity(new Intent(getContext(), ReportListActivity.class));
                } catch (Exception e) {
                    deleteTask = null;
                }
            }
        });

        return rootView;
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

    public static class AddCommentTask extends AsyncTask<Void, Void, List<CommentContent.Comment>> {

        private final String mReportId;
        private final String mContent;
        //private final String mUser;

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
                    System.out.println("comment" + c);
                    comments.add(c);
                }

                System.out.println("adap comments" + comments);
                //ArrayAdapter<String> adapter = new ArrayAdapter<String>(ReportDetailActivity.getActivity(), R.id.comment_list, comments);
                //create array adapter
                //ListView
                return comments;
            } catch (Exception e) {
                return null;
            }
        }
    }




}