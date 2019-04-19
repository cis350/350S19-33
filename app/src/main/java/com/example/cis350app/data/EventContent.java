package com.example.cis350app.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import org.json.JSONArray;
import org.json.JSONObject;

public class EventContent {

    public static final List<Event> ITEMS = new ArrayList<Event>();

    public static final Map<String, Event> ITEM_MAP = new HashMap<String, Event>();

    static {
        List<Event> events = getEventsFromDB();
        for (Event e : events) {
            addItem(e);
        }
    }

    private static void addItem(Event item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static List<Event> getEventsFromDB() {
        try {
            URL url = new URL("http://10.0.2.2:3000/getAdmins"); //fix this
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            Scanner in = new Scanner(url.openStream());
            String msg = in.nextLine();

            JSONObject jo = new JSONObject(msg);
            JSONArray arr = jo.getJSONArray("result");
            List<Event> events = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String id = obj.getString("id");
                String name = obj.getString("name");
                String location = obj.getString("gender");
                String time = obj.getString("phone");
                String host = obj.getString("email");
                String description = obj.getString("location");
                Event e = new Event(id, name, location, time, host, description);
                events.add(e);
            }
            return events;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * An event item
     */
    public static class Event {
        public final String id; // unique ID of the event
        public final String name; // name of event
        public final String location; // location of event
        public final String time; //time of event
        public final String host; //host of event
        public final String description; //description of event
        public final List<String> comments = new ArrayList<String>(); //comments on event
        public boolean register = false;

        public Event(String id, String name, String location, String time, String host,
                     String description) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.time = time;
            this.host = host;
            this.description = description;
        }

        @Override
        public String toString() {
            String s = name + "\n" + "Location: " + location + "\n" + " Time: " + time +
                    "\n" + "Host: " + host + "\n" + "Description: " + description;
            return s;
        }

        public List<String> getComments() {
            return comments;
        }

        public void addComment(String comment) {
            comments.add(comment);
        }

        public void register() {
            register = true;
        }

        public boolean isRegistered() {
            return register;
        }
    }
}
