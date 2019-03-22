package com.example.cis350app;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.view.View.MeasureSpec;
import android.content.Context;
import android.content.Intent;

import com.example.cis350app.data.EventContent;

import java.util.ArrayList;
import java.util.List;

/**
 * An activity representing a list of Events partitioned into two groups:
 * events the user already registered for and events that are available to
 * be registered for.
 */
public class EventListActivity extends AppCompatActivity {
    private ListView registeredList, unregisteredList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);

        registeredList = (ListView)findViewById(R.id.event_list_registered);
        unregisteredList = (ListView)findViewById(R.id.event_list_unregistered);

        List<EventContent.Event> registeredEvents = getRegistered().get(0);
        List<EventContent.Event> unRegisteredEvents = getRegistered().get(1);

        registeredList.setAdapter(new ArrayAdapter<EventContent.Event>(this, android.R.layout.simple_list_item_1, registeredEvents));
        unregisteredList.setAdapter(new ArrayAdapter<EventContent.Event>(this, android.R.layout.simple_list_item_1, unRegisteredEvents));

        ListUtils.setDynamicHeight(registeredList);
        ListUtils.setDynamicHeight(unregisteredList);

        registeredList.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        EventContent.Event item = (EventContent.Event) view.getTag();
                        Context context = view.getContext();
                        Intent intent = new Intent(context, EventDetailActivity.class);
                        intent.putExtra(EventDetailFragment.ARG_ITEM_ID, EventContent.ITEMS.get(i).id);

                        context.startActivity(intent);
                    }

                }

        );
    }


    public static class ListUtils {
        public static void setDynamicHeight(ListView mListView) {
            ListAdapter mListAdapter = mListView.getAdapter();
            if (mListAdapter == null) {
                // when adapter is null
                return;
            }
            int height = 0;
            int desiredWidth = MeasureSpec.makeMeasureSpec(mListView.getWidth(), MeasureSpec.UNSPECIFIED);
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                View listItem = mListAdapter.getView(i, null, mListView);
                listItem.measure(desiredWidth, MeasureSpec.UNSPECIFIED);
                height += listItem.getMeasuredHeight();
            }
            ViewGroup.LayoutParams params = mListView.getLayoutParams();
            params.height = height + (mListView.getDividerHeight() * (mListAdapter.getCount() - 1));
            mListView.setLayoutParams(params);
            mListView.requestLayout();
        }
    }

    private List<List<EventContent.Event>> getRegistered() {
        List<EventContent.Event> registeredEvents = new ArrayList<EventContent.Event>();
        List<EventContent.Event> unregisteredEvents = new ArrayList<EventContent.Event>();
        List<EventContent.Event> events = EventContent.ITEMS;
        for (int i = 0; i < events.size(); i++) {
            if (events.get(i).isRegistered()) {
                registeredEvents.add(events.get(i));
            } else {
                unregisteredEvents.add(events.get(i));
            }
        }
        List<List<EventContent.Event>> partitionedEvents = new ArrayList<List<EventContent.Event>>();
        partitionedEvents.add(registeredEvents);
        partitionedEvents.add(unregisteredEvents);
        return partitionedEvents;
    }
}
