package com.example.cis350app.data;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Notification content for the Mailbox feature
 */
public class NotificationContent {

    public static final List<Notification> ITEMS = new ArrayList<Notification>();

    public static final Map<String, Notification> ITEM_MAP = new HashMap<String, Notification>();

    static {
        List<Notification> notifs = createDummyItems();
        for (Notification n : notifs) {
            addItem(n);
        }
    }

    private static void addItem(Notification item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static List<Notification> createDummyItems() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Notification n1 = new Notification(
                "0", "An admin commented on your report", format.format(new Date()), "Report 002");
        Notification n2 = new Notification(
                "1", "An admin resolved your report", format.format(new Date()), "Report 001");
        Notification n3 = new Notification(
                "2", "An admin took action on your report", format.format(new Date()), "Report 002");
        List<Notification> notifs = new ArrayList<>();
        notifs.add(n1);
        notifs.add(n2);
        notifs.add(n3);
        return notifs;
    }

    /**
     * A notification item
     */
    public static class Notification {
        public final String id; // unique ID of the notif
        public final String content; // content of the notif
        public final String timestamp; // when the notif was sent
        public final String report; // unique ID of the report referenced in the notif

        public Notification(String id, String content, String timestamp, String report) {
            this.id = id;
            this.content = content;
            this.timestamp = timestamp;
            this.report = report;
        }

        @Override
        public String toString() {
            String s = content + "\n" + "Sent on: " + timestamp + " regarding " + report;
            return s;
        }
    }
}
