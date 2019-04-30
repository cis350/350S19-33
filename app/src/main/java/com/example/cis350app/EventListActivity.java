package com.example.cis350app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.content.Intent;

import com.example.cis350app.data.EventContent;
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
 * An activity representing a list of Events partitioned into two groups:
 * events the user already registered for and events that are available to
 * be registered for.
 */
public class EventListActivity extends AppCompatActivity {
    private static EventTask eventTask = null;
    public static ArrayList<String> ITEMS = null;
    public static ArrayList<String> ITEMS_REGISTERED = null;
    public static ArrayList<String> ITEMS_UNREGISTERED = null;
    public static Map<String, EventContent.Event> ITEM_MAP = null;
    public String currStudent;
    ListView event_list;
    ArrayAdapter<String> adapterRegistered;
    ArrayAdapter<String> adapterUnregistered;

    private ListView event_list_unregistered, event_list_registered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        event_list = (ListView) findViewById(R.id.event_list_unregistered);

        event_list_registered = (ListView)findViewById(R.id.event_list_registered);
        event_list_unregistered = (ListView)findViewById(R.id.event_list_unregistered);

        currStudent = LoginActivity.getsessionUserName();

        try {
            eventTask = new EventListActivity.EventTask();
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<>();
            ITEMS_REGISTERED = new ArrayList<>();
            ITEMS_UNREGISTERED = new ArrayList<>();
            eventTask.execute((Void) null);
            List<EventContent.Event> events = eventTask.get();
            for (EventContent.Event e : events) {
                ITEMS.add(e.name);
                ITEM_MAP.put(e.name, e);
                boolean isRegistered = false;
                for (String s : e.students) {
                    if (s.equals(currStudent)) {
                        isRegistered = true;
                    }
                }
                if (isRegistered) {
                    ITEMS_REGISTERED.add(e.name);
                } else {
                    ITEMS_UNREGISTERED.add(e.name);
                }

            }
            eventTask = null;
        } catch (Exception e) {
            eventTask = null;
        }

        adapterRegistered = new ArrayAdapter<String>(
                EventListActivity.this,
                android.R.layout.simple_list_item_1, //fix this
                ITEMS_REGISTERED
        );

        adapterUnregistered = new ArrayAdapter<String>(
                EventListActivity.this,
                android.R.layout.simple_list_item_1, //fix this
                ITEMS_UNREGISTERED
        );

        ImageButton home = (ImageButton) findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(i);
            }
        });

        //put listeners on the admin items in the listviews
        event_list_registered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventListActivity.this, EventDetailActivity.class);
                String eventSelected = event_list_registered.getItemAtPosition(position).toString();
                EventContent.Event e = getEvent(eventSelected);
                intent.putExtra("item_id", e.id);
                startActivity(intent);
            }
        });
        event_list_unregistered.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventListActivity.this, EventDetailActivity.class);
                String eventSelected = event_list_unregistered.getItemAtPosition(position).toString();
                EventContent.Event e = getEvent(eventSelected);
                intent.putExtra("item_id", e.id);
                startActivity(intent);
            }
        });

        event_list_registered.setAdapter(adapterRegistered);
        event_list_unregistered.setAdapter(adapterUnregistered);
    }

    public static EventContent.Event getEvent(String name){
        return ITEM_MAP.get(name);
    }

    /**
     * Fetch notifications
     */
    public static class EventTask extends AsyncTask<Void, Void, List<EventContent.Event>> {
        @Override
        protected List<EventContent.Event> doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/getEvents");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();
                JSONObject jo = new JSONObject(msg);
                JSONArray arr = jo.getJSONArray("result");
                System.out.println("result : " + arr);
                List<EventContent.Event> events = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String id = obj.getString("id");
                    String name = obj.getString("name");
                    String location = obj.getString("location");
                    String time = obj.getString("time");
                    String date = obj.getString("date");
                    String host = obj.getString("host");
                    String description = obj.getString("description");
                    JSONArray studentsJSON = obj.getJSONArray("students");
                    String[] students = new String[studentsJSON.length()];
                    for (int j = 0; j < studentsJSON.length(); j++) {
                        students[j] = studentsJSON.optString(j);
                    }
                    JSONArray commentsJSON = obj.getJSONArray("comments");
                    String[] comments = new String[commentsJSON.length()];
                    for (int j = 0; j < commentsJSON.length(); j++) {
                        comments[j] = commentsJSON.optString(j);
                    }
                    EventContent.Event e =
                            new EventContent.Event(id, name, location, time, date, host,
                                    description, students, comments);
                    events.add(e);
                }
                return events;
            } catch (Exception e) {
                return null;
            }
        }
    }
}