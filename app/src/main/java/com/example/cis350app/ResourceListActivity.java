package com.example.cis350app;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import com.example.cis350app.data.ResourceContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ResourceListActivity extends AppCompatActivity {
    private static ResourceTask resourceTask = null;
    public static ArrayList<String> ITEMS = new ArrayList<>();
    public static Map<String, ResourceContent.Resource> ITEM_MAP = new HashMap<String, ResourceContent.Resource>();
    ListView resource_list;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_list);

        resource_list = (ListView) findViewById(R.id.resourcesList);

        try {
            resourceTask = new ResourceListActivity.ResourceTask();
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<>();
            resourceTask.execute((Void) null);
            List<ResourceContent.Resource> resources = resourceTask.get();
            for (ResourceContent.Resource r : resources) {
                ITEMS.add(r.name);
                ITEM_MAP.put(r.name, r);
            }
            resourceTask = null;
        } catch (Exception e) {
            resourceTask = null;
        }

        adapter = new ArrayAdapter<String>(
                ResourceListActivity.this,
                android.R.layout.simple_list_item_1, //fix this
                ITEMS
        );

        //put listeners on the admin items in the listviews
        resource_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ResourceListActivity.this, ResourceDetailActivity.class);
                String resourceSelected = resource_list.getItemAtPosition(position).toString();
                ResourceContent.Resource r = getResource(resourceSelected);
                intent.putExtra("item_id", r.id);
                startActivity(intent);
            }
        });

        resource_list.setAdapter(adapter);

        ImageButton home = (ImageButton) findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(i);
            }
        });
    }

    public static ResourceContent.Resource getResource(String name){
        return ITEM_MAP.get(name);
    }

    public static class ResourceTask extends AsyncTask<Void, Void, List<ResourceContent.Resource>> {


        @Override
        protected List<ResourceContent.Resource> doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/getResources");
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

    public void create_resource(View view) {
        startActivity(new Intent(ResourceListActivity.this, ResourceCreateActivity.class));
    }
}