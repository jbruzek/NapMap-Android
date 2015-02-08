package com.example.jbruzek.myapplication;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;

/**
 * Created by jbruzek on 2/7/15.
 */
public class NapLocationsFragment extends ListFragment implements ParseCallbacks {

    private ParseHelper ph;
    private LayoutInflater inflater;
    private ArrayList<Location> list;
    private Context context;
    /**
     * initialize the interface
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ph = new ParseHelper(this);
        this.inflater = inflater;
        View v = inflater.inflate(R.layout.locations_fragment, container, false);
        container.removeAllViews();
        ListView listView = (ListView) v.findViewById(android.R.id.list);
        FloatingActionButton fab = (FloatingActionButton) v.findViewById(R.id.fab);
        fab.attachToListView(listView);
        fab.show(false);

        context = getActivity().getApplicationContext();

        ph.queryLocations();

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * get the picker, add the "Add Movie" item to the list
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void complete() {
        list = ph.getLocations();
        Log.d("score", "Length of list: " + list.size());
        Location[] locations = new Location[list.size()];
        for (int i = 0; i < list.size(); i++) {
            Log.d("Location " + i + " id:", list.get(i).id() + "");
            locations[i] = list.get(i);
        }

        LocationAdapter adapter = new LocationAdapter(inflater.getContext(), locations);
        setListAdapter(adapter);
    }

    @Override
    public void commentsComplete() {
        //nothing here
    }

    /**
     * when an item is clicked
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Location li = list.get(position);
        Log.d("intent", "Passing id " + li.id());
        Intent intent = new Intent(context, LocationActivity.class);
        intent.putExtra("id", li.id());
        intent.putExtra("title", li.title());
        startActivity(intent);
    }

    /**
     * an adapter that populates the individual list items
     *
     * @author jbruzek
     *
     */
    private class LocationAdapter extends ArrayAdapter<Location> {
        private final Context context;
        private final Location[] values;

        /**
         * create the adapter
         * @param context
         * @param values
         */
        public LocationAdapter(Context context, Location[] values) {
            super(context, R.layout.location_item, values);
            this.context = context;
            this.values = values;
        }

        /**
         * initialize the item interface
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.location_item, parent, false);
            TextView title = (TextView) rowView.findViewById(R.id.location_title);
            title.setText(values[position].title());
            TextView rating = (TextView) rowView.findViewById(R.id.location_sub_1);
            rating.setText(doubToString(values[position].rating()) + " out of 5 pillows");
            TextView fullness = (TextView) rowView.findViewById(R.id.location_sub_2);
            int f = values[position].fullness();
            if (f == 1) fullness.setText("1 person napping here now");
            else fullness.setText(f + " people napping here now");
            return rowView;
        }

        private String doubToString(Double l) {
            StringBuilder sb = new StringBuilder();
            sb.append(l);
            return sb.toString();
        }
    }
}
