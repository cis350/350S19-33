package com.example.cis350app;

import android.app.Activity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.Menu;
import android.text.TextUtils;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;


import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

import android.view.View;
import android.view.View.OnClickListener;



import com.example.cis350app.data.ReportContent;


public class ReportDetailFragment extends Fragment {
    private static ReportListActivity.ReportTask reportTask = null;
    public static ArrayList<String> ITEMS = new ArrayList<>();
    public static Map<String, ReportContent.Report> ITEM_MAP = new HashMap<String, ReportContent.Report>();

    public static final String ARG_ITEM_ID = "id";
    private ReportContent.Report mItem;

    public ReportDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            String username = LoginActivity.getsessionUserName();
            reportTask = new ReportListActivity.ReportTask(username);
            //reportTask = new ReportListActivity.ReportTask();
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<String, ReportContent.Report>();
            reportTask.execute((Void) null);
            List<ReportContent.Report> report = reportTask.get();
            //System.out.println("get report"+ report);
            for (ReportContent.Report r : report) {
                //System.out.println("get the report  " + r);
                //System.out.println("get the report id " + r.id);
                ITEMS.add(r.id);
                ITEM_MAP.put(r.id, r);
            }
            reportTask = null;
        } catch (Exception e) {
            reportTask = null;
        }

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            System.out.println("ARGS"+ getArguments());
            System.out.println("get id from:" + ARG_ITEM_ID);
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            System.out.println("mItem" + mItem);
            //mItem = ReportActivity.CreateReportTaskgetArguments();
            //System.out.println(ITEM_MAP.get(ITEM_MAPARG_ITEM_ID));
            //mItem = toString.(getArguments());
            //System.out.println(mItem);
            //mItem = NotificationListActivity.ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            System.out.println(mItem);
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                //appBarLayout.setTitle(mItem.name);
                appBarLayout.setTitle("Your Report");

            }
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.report_singleton, container, false);


        // Show the dummy content as text in a TextView.
        //if (mItem != null) {
        // ((TextView) rootView.findViewById(R.id.report_textview)).setText(mItem.toString());
        System.out.println(getArguments());
        ((TextView) rootView.findViewById(R.id.report_textview)).setText(getArguments().toString());
        //}

        return rootView;
    }

}