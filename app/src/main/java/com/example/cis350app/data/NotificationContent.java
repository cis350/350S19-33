package com.example.cis350app.data;

import org.w3c.dom.ls.LSSerializer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Notification content
 */
public class NotificationContent {

    public static final List<Notification> ITEMS = new ArrayList<Notification>();

    public static final Map<String, Notification> ITEM_MAP = new HashMap<String, Notification>();

    public static void addItem(Notification item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    /**
     * A notification item
     */
    public static class Notification implements Serializable {
        public final String id; // unique ID of the notif
        public final String content; // content of the notif
        public final String timestamp; // when the notif was sent
        public final String report; // unique ID of the report referenced in the notif

        private static int idCount = 1;

        public Notification(String content, String timestamp, String report) {
            this.id = Integer.toString(idCount);
            idCount++;
            this.content = content;
            this.timestamp = timestamp;
            this.report = report;
        }

        @Override
        public String toString() {
            String s = "Report ID: " + id + "\n" + "Content: " + content + "\n" + "Sent on: " + timestamp;
            return s;
        }
    }
}