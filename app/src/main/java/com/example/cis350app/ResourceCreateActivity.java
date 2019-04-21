package com.example.cis350app;

import android.os.AsyncTask;
import  android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.EditText;
import android.view.View;

import com.example.cis350app.data.ResourceContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import android.widget.Button;
import android.text.TextUtils;

public class ResourceCreateActivity extends AppCompatActivity {
    private TextView mTextMessage;

    private EditText name;
    private EditText description;
    private static ResourceTask resourceTask = null;
    private static String nameString = null;
    private static String descriptionString = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_resource);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button submit = (Button) findViewById(R.id.save_resource_button);

        name = (EditText) findViewById(R.id.edit_name);
        description = (EditText) findViewById(R.id.edit_description);

        submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    String n = name.getText().toString();
                    String d = description.getText().toString();
                    resourceTask = new ResourceCreateActivity.ResourceTask(n, d);
                    resourceTask.execute((Void) null);
                    resourceTask = null;
                    startActivity(new Intent(ResourceCreateActivity.this, ResourceListActivity.class));
                } catch (Exception e) {
                    resourceTask = null;
                }
            }
        });
    }

    public static class ResourceTask extends AsyncTask<Void, Void, List<ResourceContent.Resource>> {
        String currStudent = LoginActivity.getsessionUserName();
        String nameString;
        String descriptionString;

        public ResourceTask(String name, String description) {
            nameString = name;
            descriptionString = description;
        }

        @Override
        protected List<ResourceContent.Resource> doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/saveResource?name=" +
                        nameString + "&description=" + descriptionString);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
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
                    String description = obj.getString("description");
                    ResourceContent.Resource r =
                            new ResourceContent.Resource(id, name, description);
                    resources.add(r);
                }
                return resources;
            } catch (Exception e) {
                return null;
            }
        }
    }
}