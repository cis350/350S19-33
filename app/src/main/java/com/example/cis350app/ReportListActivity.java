package com.example.cis350app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.ArrayAdapter;


import com.example.cis350app.data.ReportContent;

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
    //rivate static ReportTask rT = null;


    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private static ReportTask reportTask = null;
    public static List<ReportContent.Report> ITEMS = new ArrayList<>();
    public static Map<String, ReportContent.Report> ITEM_MAP = new HashMap<String, ReportContent.Report>();
    ListView reports_list;
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
       // toolbar.setTitle(getTitle());

        reports_list = (ListView) findViewById(R.id.reportsList);


        try {
            String username = LoginActivity.getsessionUserName();
            reportTask = new ReportTask(username);
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<String, ReportContent.Report>();
            reportTask.execute((Void) null);
            List<ReportContent.Report> reports = reportTask.get();
            //System.out.println("report: "+  reports);
            for (ReportContent.Report r : reports){
                System.out.println(r);
                ITEMS.add(r);
                ITEM_MAP.put(r.name, r);
                //System.out.println("tada" + ITEM_MAP.get(r.name));

            }
            reportTask = null;
        } catch (Exception e) {
            reportTask = null;
        }
        //if (findViewById(R.id.notification_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
          //  mTwoPane = true;
        //}

        adapter = new ArrayAdapter(
                ReportListActivity.this,
                android.R.layout.simple_list_item_1, //fix this
                ITEMS
        );

       /* View recyclerView = findViewById(R.id.report_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);*/


        //reports_list.setAdapter(adapter);
        reports_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ReportListActivity.this, ReportDetailActivity.class);
                String reportSelected = reports_list.getItemAtPosition(position).toString();
                System.out.println("bah" + reportSelected);
                //ReportContent.Report r = getReport(reportSelected);
                // System.out.println("this r" + r);
                intent.putExtra("Name",reportSelected);
                //System.out.println
                startActivity(intent);
            }
        });

        reports_list.setAdapter(adapter);
    }


   /* private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ITEMS, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final ReportListActivity mParentActivity;
        private final List<ReportContent.Report> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReportContent.Report item = (ReportContent.Report) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(ReportDetailFragment.ARG_ITEM_ID, item.id);
                    ReportDetailFragment fragment = new ReportDetailFragment();
                    fragment.setArguments(arguments);
                    /*mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.notification_detail_container, fragment)
                            .commit();*/
               /* } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ReportDetailActivity.class);
                    intent.putExtra(ReportDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(ReportListActivity parent,
                                      List<ReportContent.Report> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.report_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mContentView.setText(mValues.get(position).name);

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view) {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.id_text);
                mContentView = (TextView) view.findViewById(R.id.content);
            }
        }
    }
*/
    public static ReportContent.Report getReport(String name){

        return ITEM_MAP.get(name);
    }

    /**
     * Fetch notifications
     */
    public static class ReportTask extends AsyncTask<Void, Void, List<ReportContent.Report>> {

        private final String mUsername;

        ReportTask(String username) {
            mUsername = username;
        }
        @Override
        protected List<ReportContent.Report> doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/getReport?username=" + mUsername);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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
                    /*JSONArray commentsJSON = obj.getJSONArray("comments");
                    List<String> comments = new ArrayList<String>(){};
                    for (int j = 0; j < commentsJSON.length(); j++) {
                        comments.add(commentsJSON.optString(j));
                    }*/
                    ReportContent.Report r = new ReportContent.Report(id, username, name, date, subject, description, person);
                    System.out.println(r.username);
                    System.out.println(r.description);
                    reports.add(r);
                }
                return reports;
            } catch (Exception e) {
                return null;
            }
        }

    }
}

