package com.example.cis350app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.cis350app.data.ResourceContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ResourceCreateActivity extends AppCompatActivity {
    private TextView mTextMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_resource);
    }

    /*

    public void save_report(View view) {
        //String name = (String) R.id.edit_name;
        //String description = (String) R.id.edit_description;
    }
    */

    /**
     * Fetch resources
     */
    public static class ResourcesTask extends AsyncTask<Void, Void, List<ResourceContent.Resource>> {

        private final String mName;
        private final String mDescription;

        public ResourcesTask(String name, String description) {
            mName = name;
            mDescription = description;
        }

        @Override
        protected List<ResourceContent.Resource> doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/createResource?name=" + mName
                        + "&&description=" + mDescription);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                conn.connect();

                //fix below

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();

                JSONObject jo = new JSONObject(msg);
                JSONArray arr = jo.getJSONArray("result");
                List<ResourceContent.Resource> notifs = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String content = obj.getString("content");
                    String reportId = obj.getString("reportId");
                    String date = obj.getString("date");
                    ResourceContent.Resource n = new ResourceContent.Resource(content, date, reportId);
                    notifs.add(n);
                }
                return notifs;
            } catch (Exception e) {
                return null;
            }
        }
    }
}