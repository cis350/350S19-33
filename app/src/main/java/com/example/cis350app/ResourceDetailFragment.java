package com.example.cis350app;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

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

public class ResourceDetailFragment extends Fragment {
    private static ResourceListActivity.ResourceTask resourceTask = null;
    public static ArrayList<String> ITEMS = new ArrayList<>();
    public static Map<String, ResourceContent.Resource> ITEM_MAP = new HashMap<String, ResourceContent.Resource>();

    public static final String ARG_ITEM_ID = "item_id";
    private ResourceContent.Resource mItem;

    public ResourceDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            resourceTask = new ResourceListActivity.ResourceTask();
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<String, ResourceContent.Resource>();
            resourceTask.execute((Void) null);
            List<ResourceContent.Resource> resources = resourceTask.get();
            for (ResourceContent.Resource r : resources) {
                ITEMS.add(r.id);
                ITEM_MAP.put(r.id, r);
            }
            resourceTask = null;
        } catch (Exception e) {
            resourceTask = null;
        }

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = ITEM_MAP.get(getArguments().getString(ARG_ITEM_ID));
            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.name);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.resource_singleton, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.resource_textview)).setText(mItem.toString());
        }

        return rootView;
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

}
