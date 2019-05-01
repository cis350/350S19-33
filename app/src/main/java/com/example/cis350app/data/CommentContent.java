package com.example.cis350app.data;

import android.text.format.DateFormat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommentContent {

    public static final List<Comment> ITEMS = new ArrayList<Comment>();

    public static final Map<String, Comment> ITEM_MAP = new HashMap<String, Comment>();

    public static void addItem(CommentContent.Comment item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }


    /*private static List<ReportContent.Report> createDummyItems() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ReportContent.Report r1 = new Report(
                "0", "thom", "Thomas", format.format(new Date()), "Bullyiny",
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
    }*/

    /**
     * A report item
     */
    public static class Comment{
        public final String id; // unique ID of the report
        public final String reportId;
        public final String content; //optional input for name, otherwise default is anon
        public final String user; //date report submitted
        public final String role; // subject of report
        public final String date; //description of incident


        public Comment(String id, String reportId, String content, String user, String role, String date) {
            this.id = id;
            this.reportId = reportId;
            this.content = content;
            this.user = user;
            this.role = role;
            this.date = date;
            //this.person = person;
            //this.comments = comments;
        }

        @Override
        public String toString() {
            //String comments = commentString();
            String c = role + ":" +content;
            return c;
        }


    }
}
