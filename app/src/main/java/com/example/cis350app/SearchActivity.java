package com.example.cis350app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.ListView;
import java.util.Arrays;
import java.util.ArrayList;
import com.example.cis350app.data.SearchContent.Profile;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class SearchActivity extends AppCompatActivity {

    ListView search_admin;
    ArrayAdapter<String> adapter;

    private static ProfileTask profileTask = null;
    public static ArrayList<String> ITEMS = new ArrayList<>();
    public static Map<String, Profile> ITEM_MAP = new HashMap<String, Profile>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        search_admin = (ListView) findViewById(R.id.search_admin);

        try {
            profileTask = new ProfileTask();
            ITEMS = new ArrayList<>();
            ITEM_MAP = new HashMap<String, Profile>();
            profileTask.execute((Void) null);
            List<Profile> admins = profileTask.get();
            for (Profile n : admins) {
                ITEMS.add(n.name);
                ITEM_MAP.put(n.name, n);
            }
            profileTask = null;
        } catch (Exception e) {
            profileTask = null;
        }

        ArrayList<String> arrayAdmin = new ArrayList<>();
        arrayAdmin.addAll(Arrays.asList(getResources().getStringArray(R.array.my_admins)));

        adapter = new ArrayAdapter<String>(
                SearchActivity.this,
                android.R.layout.simple_list_item_1,
               // arrayAdmin <- OLD CODE
                ITEMS
        );

        //put listeners on the admin items in the listviews
        search_admin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, ProfileActivity.class);
                String adminSelected = search_admin.getItemAtPosition(position).toString();
                Profile adminProfile = getAdminData(adminSelected);
                intent.putExtra("AdminName", adminProfile);
                startActivity(intent);
            }
        });

        search_admin.setAdapter(adapter);

    }

    //takes in the admin's full name and returns profile data
    public static Profile getAdminData(String name){
        return ITEM_MAP.get(name);
    }

    //create a search bar that filters based on input
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem item = menu.findItem(R.id.search_admin);
        SearchView searchView = (SearchView)item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Fetch notifications
     */
    public static class ProfileTask extends AsyncTask<Void, Void, List<Profile>> {

        @Override
        protected List<Profile> doInBackground(Void... params) {
            try {
                URL url = new URL("http://10.0.2.2:3000/getAdmins");
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("GET");
                conn.connect();

                Scanner in = new Scanner(url.openStream());
                String msg = in.nextLine();

                JSONObject jo = new JSONObject(msg);
                JSONArray arr = jo.getJSONArray("result");
                List<Profile> admins = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    String name = obj.getString("name");
                    String phone = obj.getString("phone");
                    String email = obj.getString("email");
                    String location = obj.getString("location");
                    Profile n = new Profile(name, "counselor", "female", phone, email, location);
                    admins.add(n);
                }
                return admins;
            } catch (Exception e) {
                return null;
            }
        }
    }
}