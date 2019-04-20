package com.example.cis350app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    public static ArrayList<String> ITEMS = new ArrayList<>();
    public static Map<String, EventContent.Event> ITEM_MAP = new HashMap<String, EventContent.Event>();
    //public String currStudent = LoginActivity.getsessionUserName();
    ListView event_list;
    ArrayAdapter<String> adapter;

    private ListView registeredList, unregisteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        event_list = (ListView) findViewById(R.id.event_list_unregistered);

        try {
            eventTask = new EventListActivity.EventTask();
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<String, EventContent.Event>();
            eventTask.execute((Void) null);
            List<EventContent.Event> events = eventTask.get();
            for (EventContent.Event e : events) {
                ITEMS.add(e.name);
                ITEM_MAP.put(e.name, e);
            }
            eventTask = null;
        } catch (Exception e) {
            eventTask = null;
        }

        adapter = new ArrayAdapter<String>(
                EventListActivity.this,
                android.R.layout.simple_list_item_1, //fix this
                ITEMS
        );

        //put listeners on the admin items in the listviews
        event_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventListActivity.this, EventDetailActivity.class);
                String eventSelected = event_list.getItemAtPosition(position).toString();
                EventContent.Event e = getEvent(eventSelected);
                intent.putExtra("item_id", e.id);
                startActivity(intent);
            }
        });

        event_list.setAdapter(adapter);
    }

    public static EventContent.Event getEvent(String name){
        return ITEM_MAP.get(name);
    }

    /*

        registeredList = (ListView)findViewById(R.id.event_list_registered);
        unregisteredList = (ListView)findViewById(R.id.event_list_unregistered);

        List<EventContent.Event> registeredEvents = getRegistered().get(0);
        List<EventContent.Event> unRegisteredEvents = getRegistered().get(1);

        registeredList.setAdapter(new ArrayAdapter<EventContent.Event>(this, android.R.layout.simple_list_item_1, registeredEvents));
        unregisteredList.setAdapter(new ArrayAdapter<EventContent.Event>(this, android.R.layout.simple_list_item_1, unRegisteredEvents));

        ListUtils.setDynamicHeight(registeredList);
        ListUtils.setDynamicHeight(unregisteredList);

        registeredList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        EventContent.Event item = (EventContent.Event) view.getTag();
                        Context context = view.getContext();
                        Intent intent = new Intent(context, EventDetailActivity.class);
                        intent.putExtra(EventDetailFragment.ARG_ITEM_ID, EventContent.ITEMS.get(i).id);
                        context.startActivity(intent);
                    }

                }

        );
    }

    */

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
                List<EventContent.Event> events = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String id = obj.getString("id");
                    String name = obj.getString("name");
                    String location = obj.getString("location");
                    String time = obj.getString("time");
                    String host = obj.getString("host");
                    String description = obj.getString("description");
                    JSONArray studentsJSON = obj.getJSONArray("students");
                    String[] students = new String[studentsJSON.length()];
                    for (int j = 0; j < studentsJSON.length(); j++) {
                        students[j] = studentsJSON.optString(j);
                    }
                    EventContent.Event e =
                            new EventContent.Event(id, name, location, time, host, description, students);
                    events.add(e);
                }
                return events;
            } catch (Exception e) {
                return null;
            }
        }
    }

/*
    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = MeasureSpec.makeMeasureSpec(mListView.getWidth(), MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    private List<List<EventContent.Event>> getRegistered() {
        List<EventContent.Event> registeredEvents = new ArrayList<EventContent.Event>();
        List<EventContent.Event> unregisteredEvents = new ArrayList<EventContent.Event>();
        List<EventContent.Event> events = EventContent.ITEMS;
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).isRegistered()) {
                registeredEvents.add(events.get(i));
            } else {
                unregisteredEvents.add(events.get(i));
            }
        }
        List<List<EventContent.Event>> partitionedEvents = new ArrayList<List<EventContent.Event>>();
        partitionedEvents.add(registeredEvents);
        partitionedEvents.add(unregisteredEvents);
        return partitionedEvents;
    }
    */
}