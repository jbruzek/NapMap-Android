package com.example.jbruzek.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

/**
 * Created by jbruzek on 2/7/15.
 */
public class MapFragment extends Fragment implements ParseCallbacks {

    private MapView mapView;
    private GoogleMap map;
    private ParseHelper ph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);
        container.removeAllViews();

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        //try {
            MapsInitializer.initialize(this.getActivity());
        //} catch (GooglePlayServicesNotAvailableException e) {
        //    e.printStackTrace();
        //}

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.22666, -80.4209), 15);
        map.animateCamera(cameraUpdate);

        ph = new ParseHelper(this);
        ph.queryLocations();

        return v;
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

    @Override
    public void complete() {
        ArrayList<Location> li = ph.getLocations();
        for (Location l : li) {
            map.addMarker(new MarkerOptions().position(new LatLng(l.latitude(), l.longitude())).title(l.title()).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_red_pillow)));
        }
    }

    @Override
    public void commentsComplete() {
        //nothing here
    }
}
