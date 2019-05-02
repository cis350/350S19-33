package com.example.cis350app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.example.cis350app.data.NotificationContent;

/**
 * An activity representing a single Notification detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link NotificationListActivity}.
 */
public class NotificationDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_singleton);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();

            NotificationContent.Notification notifInfo = (NotificationContent.Notification)
                    getIntent().getSerializableExtra("notification");
            arguments.putSerializable("notif", notifInfo);
            // arguments.putString(NotificationDetailFragment.ARG_ITEM_ID, notifInfo.toString());
            NotificationDetailFragment fragment = new NotificationDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.notification_singleton_container, fragment)
                    .commit();
        }
        ImageButton home = (ImageButton) findViewById(R.id.home_button);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), HomeActivity.class);
                startActivity(i);
            }
        });

    }

}
