package com.example.jbruzek.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class LocationActivity extends Activity implements ParseCallbacks {

    private ParseHelper ph;
    private TextView title;
    private TextView description;
    private RatingBar rating;
    private ListView listView;
    private GoogleMap map;
    private MapView mapView;
    private ArrayList<String> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_main);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) findViewById(R.id.mapview_header);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        MapsInitializer.initialize(this);



        title = (TextView)findViewById(R.id.location_title);
        description = (TextView)findViewById(R.id.location_description);
        rating = (RatingBar)findViewById(R.id.ratingBar);
        listView = (ListView)findViewById(R.id.comments);

        ph = new ParseHelper(this);

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        ph.queryById(id);
        ph.queryComments(id);
        String nap_title = intent.getStringExtra("title");
        title.setText(nap_title);
        setTitle(nap_title);
        getActionBar().setDisplayShowHomeEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.location, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void complete() {
        Location l = ph.currentLocation();
        Log.d("Got location", "Description: " + l.description());
        description.setText(l.description());
        rating.setRating((float)l.rating());
        map.addMarker(new MarkerOptions().position(new LatLng(l.latitude(), l.longitude())).title(l.title()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_red_pillow)));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(l.latitude(), l.longitude()), 17);
        map.animateCamera(cameraUpdate);
    }

    @Override
    public void commentsComplete() {
        comments = ph.getComments();
        Log.d("comments Complete", "Size: " + comments.size());
        String[] cList = new String[comments.size()];
        for (int i = 0; i < comments.size(); i++) {
            cList[i] = comments.get(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cList);
        listView.setAdapter(adapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }


//    /**
//     * an adapter that populates the individual list items
//     *
//     * @author jbruzek
//     *
//     */
//    private class CommentAdapter extends ArrayAdapter<String> {
//        private final Context context;
//        private final String[] values;
//
//        /**
//         * create the adapter
//         * @param context
//         * @param values
//         */
//        public CommentAdapter(Context context, String[] values) {
//            super(context, R.layout.location_item, values);
//            this.context = context;
//            this.values = values;
//        }
//
//        /**
//         * initialize the item interface
//         */
//        @Override
//        public View getView(int position, View convertView, ViewGroup parent) {
//            LayoutInflater inflater = (LayoutInflater) context
//                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View rowView = inflater.inflate(R.layout.comment, parent, false);
//            TextView title = (TextView) rowView.findViewById(R.id.comment_text);
//            title.setText(values[position]);
//
//            return rowView;
//        }
//    }
}
