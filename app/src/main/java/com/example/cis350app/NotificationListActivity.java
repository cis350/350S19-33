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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cis350app.data.NotificationContent;
import com.example.cis350app.data.NotificationContent.Notification;
import com.example.cis350app.data.ResourceContent;
import com.example.cis350app.data.SearchContent;

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
    private static NotificationsTask notifTask = null;
    public static List<String> ITEMS = new ArrayList<>();
    public static Map<String, Notification> ITEM_MAP = new HashMap<String, Notification>();
    ListView notification_list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list);

        notification_list = (ListView) findViewById(R.id.notification_list);

        try {
            String username = LoginActivity.getsessionUserName();
            notifTask = new NotificationsTask(username);
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<String, Notification>();
            notifTask.execute((Void) null);
            List<Notification> notifs = notifTask.get();
            for (Notification n : notifs) {
                System.out.println(n);
                ITEMS.add(n.content);
                ITEM_MAP.put(n.content, n);
            }
            notifTask = null;
        } catch (Exception e) {
            notifTask = null;
        }

        adapter = new ArrayAdapter<String>(
                NotificationListActivity.this,
                android.R.layout.simple_list_item_1, //fix this
                ITEMS
        );

        //put listeners on the admin items in the listviews
        /*
        notification_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NotificationListActivity.this, NotificationDetailActivity.class);
                String resourceSelected = notification_list.getItemAtPosition(position).toString();
                System.out.println("Resource selected : " + resourceSelected);
                NotificationContent.Notification n = getNotification(resourceSelected);
                System.out.println("IN NOTIFICATION LIST ACTIVITY, LISTENING ON ITEMS");

                System.out.println("n id: " + n.id);
                System.out.println("n notification: " + n.toString());
                intent.putExtra("item_id", n.id);
                intent.putExtra("item_map", n);
                startActivity(intent);
            }
        });
*/
        //put listeners on the admin items in the listviews
        notification_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(NotificationListActivity.this, NotificationDetailActivity.class);
                String resourceSelected = notification_list.getItemAtPosition(position).toString();
                NotificationContent.Notification n = getNotification(resourceSelected);
                intent.putExtra("notification", n);
                startActivity(intent);
            }
        });

        notification_list.setAdapter(adapter);

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

    public static NotificationContent.Notification getNotification(String name){
        return ITEM_MAP.get(name);
    }

    public void home_button(View view) {
        startActivity(new Intent(NotificationListActivity.this, HomeActivity.class));
    }
}