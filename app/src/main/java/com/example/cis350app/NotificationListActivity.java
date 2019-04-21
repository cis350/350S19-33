package com.example.cis350app;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.cis350app.data.NotificationContent;
import com.example.cis350app.data.NotificationContent.Notification;

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
public class NotificationListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private static NotificationsTask notifTask = null;
    public static List<Notification> ITEMS = new ArrayList<>();
    public static Map<String, Notification> ITEM_MAP = new HashMap<String, Notification>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        try {
            String username = LoginActivity.getsessionUserName();
            notifTask = new NotificationsTask(username);
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<String, Notification>();
            notifTask.execute((Void) null);
            List<Notification> notifs = notifTask.get();
            for (Notification n : notifs) {
                ITEMS.add(n);
                ITEM_MAP.put(n.id, n);
            }
            notifTask = null;
        } catch (Exception e) {
            notifTask = null;
        }


        if (findViewById(R.id.notification_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        View recyclerView = findViewById(R.id.notification_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(this, ITEMS, mTwoPane));
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final NotificationListActivity mParentActivity;
        private final List<Notification> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification item = (Notification) view.getTag();
                if (mTwoPane) {
                    Bundle arguments = new Bundle();
                    arguments.putString(NotificationDetailFragment.ARG_ITEM_ID, item.id);
                    NotificationDetailFragment fragment = new NotificationDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.notification_detail_container, fragment)
                            .commit();
                } else {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, NotificationDetailActivity.class);
                    intent.putExtra(NotificationDetailFragment.ARG_ITEM_ID, item.id);

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(NotificationListActivity parent,
                                      List<Notification> items,
                                      boolean twoPane) {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notification_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mContentView.setText(mValues.get(position).content);

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

    /**
     * Fetch notifications
     */
    public static class NotificationsTask extends AsyncTask<Void, Void, List<Notification>> {

        private final String mUsername;

        NotificationsTask(String username) {
            mUsername = username;
        }
        @Override
        protected List<Notification> doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/getNotifs?username=" + mUsername);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();

                JSONObject jo = new JSONObject(msg);
                JSONArray arr = jo.getJSONArray("result");
                List<Notification> notifs = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String content = obj.getString("content");
                    String reportId = obj.getString("reportId");
                    String date = obj.getString("date");
                    Notification n = new Notification(content, date, reportId);
                    notifs.add(n);
                }
                return notifs;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
