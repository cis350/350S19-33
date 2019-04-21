package com.example.cis350app.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventContent {

    public static final List<Event> ITEMS = new ArrayList<Event>();

    public static final Map<String, Event> ITEM_MAP = new HashMap<String, Event>();

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
        public final String[] comments; //comments on event
        public final String[] students; //students registered for the event

        public Event(String id, String name, String location, String time, String host,
                     String description, String[] students, String[] comments) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.time = time;
            this.host = host;
            this.description = description;
            this.students = students;
            this.comments = comments;
        }

        @Override
        public String toString() {
            String s = name + "\n" + "Location: " + location + "\n" + "Time: " + time +
                    "\n" + "Host: " + host + "\n" + "Description: " + description;
            return s;
        }

        public String commentString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < comments.length; i++) {
                sb.append(comments[i]);
                sb.append("\n");
            }
            return sb.toString();
        }
    }
}