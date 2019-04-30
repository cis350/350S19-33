package com.example.cis350app.data;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ResourceContent {
    public static final List<ResourceContent.Resource> ITEMS =
            new ArrayList<ResourceContent.Resource>();

    public static final Map<String, ResourceContent.Resource> ITEM_MAP =
            new HashMap<String, ResourceContent.Resource>();

    static {
        List<ResourceContent.Resource> resources = getResourcesFromDB();
        for (ResourceContent.Resource r : resources) {
            addItem(r);
        }
    }

    private static void addItem(ResourceContent.Resource item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.id, item);
    }

    private static List<ResourceContent.Resource> getResourcesFromDB() {
        try {
            URL url = new URL("http://10.0.2.2:3000/getAdmins"); //fix this
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            Scanner in = new Scanner(url.openStream());
            String msg = in.nextLine();

            JSONObject jo = new JSONObject(msg);
            JSONArray arr = jo.getJSONArray("result");
            List<ResourceContent.Resource> resources = new ArrayList<>();
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                String id = obj.getString("id");
                String name = obj.getString("name");
                String description = obj.getString("location");
                ResourceContent.Resource r = new ResourceContent.Resource(id, name, description);
                resources.add(r);
            }
            return resources;
        } catch (Exception e) {
            return null;
        }
    }


    /**
     * An event item
     */
    public static class Resource {
        public final String id; // unique ID of the resource
        public final String name; // name of resource
        public final String description; //description of resource
        public boolean register = false;

        public Resource(String id, String name, String description) {
            this.id = id;
            this.name = name;
            this.description = description;
        }

        @Override
        public String toString() {
            String s = name + "qzxq" + "Description: \n" + description;
            return s;
        }

        public String getName() {
            return name;
        }
    }
}