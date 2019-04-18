package com.example.cis350app.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchContent {
    public static final List<SearchContent.Profile> ITEMS = new ArrayList<SearchContent.Profile>();

    public static final Map<String, SearchContent.Profile> ITEM_MAP = new HashMap<String, SearchContent.Profile>();

    public static void addItem(Notification item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }
//
//    static {
//        SearchContent.Profile n1 = new SearchContent.Profile("Michelle Lok", "Teacher",
//                "Female", "3232869133", "hlok@wharton.upenn.edu",
//                "JMHH102");
//        SearchContent.Profile n2 = new SearchContent.Profile("Chelsey Lin", "Counselor",
//                "Female", "32392932939", "clin@seas.upenn.edu",
//                "JMHH103");
//        SearchContent.Profile n3 = new SearchContent.Profile("Dorothy Chang", "Vice Principal",
//                "Female", "32392932939", "dchang@seas.upenn.edu",
//                "JMHH203");
//        SearchContent.Profile n4 = new SearchContent.Profile("April Chen", "Principal",
//                "Female", "32392932939", "achen@seas.upenn.edu",
//                "JMHH503");
//
//        //add to global variables
//        ITEMS.add(n1);
//        ITEMS.add(n2);
//        ITEMS.add(n3);
//        ITEMS.add(n4);
//
//        //add to map
//        ITEM_MAP.put(n1.name, n1);
//        ITEM_MAP.put(n2.name, n2);
//        ITEM_MAP.put(n3.name, n3);
//        ITEM_MAP.put(n4.name, n4);
//    }

    /**
     * A notification item
     */
    public static class Profile implements Serializable {
        public final String name; // full name
        public final String role; // role of admin
        public final String gender; // pronouns
        public final String phone; // phone number
        public final String email; //email address
        public final String location; //officelocation

        public Profile(String name, String role, String gender, String phone, String email, String location) {
            this.name = name;
            this.role = role;
            this.gender = gender;
            this.phone = phone;
            this.email = email;
            this.location = location;
        }

    }
}
