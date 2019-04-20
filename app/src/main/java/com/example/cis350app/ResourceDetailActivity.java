package com.example.cis350app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ResourceDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.resource_singleton);

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString(ResourceDetailFragment.ARG_ITEM_ID,
                    getIntent().getStringExtra(ResourceDetailFragment.ARG_ITEM_ID));
            ResourceDetailFragment fragment = new ResourceDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.resource_singleton_container, fragment)
                    .commit();
        }
    }
}
