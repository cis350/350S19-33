package com.example.cis350app;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.os.AsyncTask;
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
import android.widget.ListView;
import android.widget.ArrayAdapter;

public class EventDetailFragment extends Fragment {
    private static EventListActivity.EventTask eventTask = null;
    public static ArrayList<String> ITEMS = new ArrayList<>();
    public static Map<String, EventContent.Event> ITEM_MAP = new HashMap<String, EventContent.Event>();
    //public String currStudent = LoginActivity.getsessionUserName();
    ListView event_list;
    ArrayAdapter<String> adapter;

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private EventContent.Event mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            eventTask = new EventListActivity.EventTask();
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<String, EventContent.Event>();
            eventTask.execute((Void) null);
            List<EventContent.Event> events = eventTask.get();
            for (EventContent.Event e : events) {
                ITEMS.add(e.id);
                ITEM_MAP.put(e.id, e);
            }
            eventTask = null;
        } catch (Exception e) {
            eventTask = null;
        }

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.event_singleton, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.event_string)).setText(mItem.toString());
            if (mItem.comments != null) {
                System.out.println("mitem has comments");
                System.out.println("mitem comment string: " + mItem.commentString());
                ((TextView) rootView.findViewById(R.id.comment_box)).setText(mItem.commentString());
            }
        }
        return rootView;
    }

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
                    JSONArray commentsJSON = obj.getJSONArray("comments");
                    String[] comments = new String[commentsJSON.length()];
                    for (int j = 0; j < commentsJSON.length(); j++) {
                        comments[j] = commentsJSON.optString(j);
                    }
                    EventContent.Event e =
                            new EventContent.Event(id, name, location, time, host, description, students, comments);
                    events.add(e);
                }
                return events;
            } catch (Exception e) {
                return null;
            }
        }
    }
}
