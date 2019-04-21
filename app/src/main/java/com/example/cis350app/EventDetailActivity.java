package com.example.cis350app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.os.AsyncTask;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.example.cis350app.data.EventContent;

import org.json.JSONArray;
import org.json.JSONObject;

public class EventDetailActivity extends AppCompatActivity {
    private static EventTask eventTask = null;
    private static String eventID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_singleton);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            eventID = getIntent().getStringExtra(EventDetailFragment.ARG_ITEM_ID);
            arguments.putString(EventDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(EventDetailFragment.ARG_ITEM_ID));
            EventDetailFragment fragment = new EventDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.event_singleton_container, fragment)
                    .commit();
        }

        Button register = (Button) findViewById(R.id.button);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    eventTask = new EventDetailActivity.EventTask();
                    eventTask.execute((Void) null);
                    eventTask = null;
                } catch (Exception e) {
                    eventTask = null;
                }
            }
        });

        EditText comment = (EditText) findViewById(R.id.new_comment);
    }

    public static class EventTask extends AsyncTask<Void, Void, List<EventContent.Event>> {
        String currStudent = LoginActivity.getsessionUserName();
        @Override
        protected List<EventContent.Event> doInBackground(Void... params) {
            try {
                System.out.println("url: " + "http://10.0.2.2:3000/registerStudent?id=" +
                        eventID + "&username=" + currStudent);
                URL url = new URL("http://10.0.2.2:3000/registerStudent?id=" +
                        eventID + "&username=" + currStudent);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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
