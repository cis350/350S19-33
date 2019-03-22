package com.example.cis350app.data;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportContent {

    public static final List<Report> ITEMS = new ArrayList<Report>();

    public static final Map<String, Report> ITEM_MAP = new HashMap<String, Report>();

    static {
        List<Report> reports = createDummyItems();
        for (Report r : reports) {
            addItem(r);
        }
    }

    private static void addItem(Report item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static List<ReportContent.Report> createDummyItems() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ReportContent.Report r1 = new Report(
                "0", "Thomas", format.format(new Date()), "Bullyiny",
                "I was bullied in the school cafeteria. I've tried to talk to the person " +
                        "who bullied me, and it's making me really anxious.", "Myself");
        ReportContent.Report r2 = new Report(
                "1", "Emily", format.format(new Date()),"Friend cyberbullied on Facebook",
                "A girl in my grade has been sending spreading really mean rumors about my best friend. " +
                "No teachers are intervening, and I would like to have administrators " +
                "intervene in the situation.","Friend of victim");
        ReportContent.Report r3 = new Report(
                "2", "Sarah", format.format(new Date()), "Bullying", "I was bullied by a group of older girls.",
                "Myself");
        List<ReportContent.Report> reports = new ArrayList<>();
        reports.add(r1);
        reports.add(r2);
        reports.add(r3);
        return reports;
    }

    /**
     * A report item
     */
    public static class Report{
        public final String id; // unique ID of the report
        public final String name; //optional input for name, otherwise default is anon
        public final String date; //date report submitted
        public final String subject; // subject of report
        public final String description; //description of incident
        public final String person; //TODO make boolean
        public final List<String> comments = new ArrayList<String>(); //comments on

        public Report(String id, String name, String date, String subject, String description,
                      String person) {
            this.id = id;
            this.name = name;
            this.date = date;
            this.subject = subject;
            this.description = description;
            this.person = person;
        }

        @Override
        public String toString() {
            String s = name + "\n" + "Date: " + date + " Subject: " + subject +
                    "Description: " + description + "Person" + person;
            return s;
        }

        public void addComment(String comment) {
            comments.add(comment);
        }
    }
}
