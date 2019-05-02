package com.example.cis350app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.cis350app.data.NotificationContent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A fragment representing a single Notification detail screen.
 * This fragment is either contained in a {@link NotificationListActivity}
 * in two-pane mode (on tablets) or a {@link NotificationDetailActivity}
 * on handsets.
 */
public class NotificationDetailFragment extends Fragment {
    public static ArrayList<String> ITEMS = new ArrayList<>();
    public static Map<String, NotificationContent.Notification> ITEM_MAP = new HashMap<String, NotificationContent.Notification>();

    private NotificationContent.Notification mItem;

    public NotificationDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the dummy content specified by the fragment
        // arguments. In a real-world scenario, use a Loader
        // to load content from a content provider.
        mItem = (NotificationContent.Notification) getArguments().getSerializable("notif");
        Activity activity = this.getActivity();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.notification_singleton, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            String s = mItem.toString();
            ((TextView) rootView.findViewById(R.id.notif_text)).setText(s);
            ((ImageButton) rootView.findViewById(R.id.home_button)).
                    setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent i = new Intent(getContext(), HomeActivity.class);
                            startActivity(i);
                        }
                    });
        }
        return rootView;
    }

}
