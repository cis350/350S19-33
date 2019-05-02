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

import com.example.cis350app.data.ReportContent.Report;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ReportDetailFragment extends Fragment {
    public static ArrayList<String> ITEMS = new ArrayList<>();
    public static Map<String, Report> ITEM_MAP = new HashMap<>();
    private static String commentString = null;
    private static DeleteReportTask deleteTask = null;

    private Report mItem;

    public ReportDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the dummy content specified by the fragment
        // arguments. In a real-world scenario, use a Loader
        // to load content from a content provider.
        mItem = (Report) getArguments().getSerializable("report");
        System.out.println("mitem: " + mItem.toString());
        Activity activity = this.getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_singleton, container, false);

        Activity activity = this.getActivity();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.subject)).setText("Subject: " + mItem.subject);
            ((TextView) rootView.findViewById(R.id.closed)).setText("Closed: " + Boolean.toString(mItem.closed));
            ((TextView) rootView.findViewById(R.id.student)).setText("Student Username: " + mItem.username);
            ((TextView) rootView.findViewById(R.id.date)).setText("Date: " + mItem.date);
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
        /*
        Button btnSubmitComment = (Button) findViewById(R.id.submit_comment_button);
        final EditText newComment = (EditText) findViewById(R.id.new_comment);

        btnSubmitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    commentString = newComment.getText().toString();
                    Log.v("6", "" + commentString);
                    addCommentTask = new AddCommentTask(id, commentString);
                    Log.v("7", "string");
                    addCommentTask.execute((Void) null);
                    addCommentTask = null;
                    Log.v("9", "messy");
                    //comments.add()
                    //create addCommentTask()
                    //create set On Click Listener and then add that comment to the comments list

                    startActivity(new Intent(ReportDetailActivity.this, ReportListActivity.class));
                } catch (Exception e) {
                    commentTask = null;
                }
            }
        });
        */

        //edit button
        Button btnEdit = (Button) rootView.findViewById(R.id.edit_submit_button);
        final EditText mEdit = (EditText) rootView.findViewById(R.id.report_detail);


        //delete button
        Button btnDelete = (Button) rootView.findViewById(R.id.delete_button);

        btnDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
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