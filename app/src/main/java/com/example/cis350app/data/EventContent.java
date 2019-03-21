package com.example.cis350app.data;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventContent {

    public static final List<Event> ITEMS = new ArrayList<Event>();

    public static final Map<String, Event> ITEM_MAP = new HashMap<String, Event>();

    static {
        List<Event> events = createDummyItems();
        for (Event e : events) {
            addItem(e);
        }
    }

    private static void addItem(Event item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static List<Event> createDummyItems() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Event e1 = new Event("0", "Mindfulness Workshop", "CAPS",
                format.format(new Date()), "A workshop teaching mindfulness exercises");
        Event e2 = new Event("1", "Addressing intergenerational trauma",
                "Houston Hall", format.format(new Date()), "Group therapy");
        Event e3 = new Event("2", "Coming Out Of The Closet", "LGBT Center",
                format.format(new Date()), "Diving into gender and sexual identities");
        List<Event> events = new ArrayList<>();
        events.add(e1);
        events.add(e2);
        events.add(e3);
        return events;
    }

    /**
     * An event item
     */
    public static class Event {
        public final String id; // unique ID of the event
        public final String name; // name of event
        public final String location; // location of event
        public final String time; //time of event
        public final String description; //description of event
        public final List<String> comments = new ArrayList<String>(); //comments on event

        public Event(String id, String name, String location, String time, String description) {
            this.id = id;
            this.name = name;
            this.location = location;
            this.time = time;
            this.description = description;
        }

        @Override
        public String toString() {
            String s = name + "\n" + "Location: " + location + " Time: " + time +
                    "Description: " + description;
            return s;
        }

        public void addComment(String comment) {
            comments.add(comment);
        }
    }
}
