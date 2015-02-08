package com.bruzek.jbruzek.myapplication;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by jbruzek on 2/7/15.
 */
public class AddLocationFragment extends Fragment implements ParseCallbacks {

    private MapView mapView;
    private GoogleMap map;
    private Button cancel;
    private Button submit;
    private EditText name;
    private EditText description;
    private LatLng location = null;
    private View v;
    private ParseHelper ph;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.add_location_fragment, container, false);
        container.removeAllViews();

        ph = new ParseHelper(this);

        // Gets the MapView from the XML layout and creates it
        mapView = (MapView) v.findViewById(R.id.mapview_add);
        mapView.onCreate(savedInstanceState);

        // Gets to GoogleMap from the MapView and does initialization stuff
        map = mapView.getMap();
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.setMyLocationEnabled(true);

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {
                double lat = latLng.latitude;
                double lng = latLng.longitude;
                if (lng > -80.401498 || lng < -80.459562
                        || lat < 37.188551 || lat > 37.237151) {
                    //this is an invalid location!
                    Toast.makeText(v.getContext(), "Invalid Location", Toast.LENGTH_SHORT).show();
                    Log.d("Lat:", "" + lat);
                    Log.d("Lng:", "" + lng);
                }
                else {
                    map.clear();
                    location = latLng;
                    map.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_pillow_pin)));
                }
            }
        });

        // Needs to call MapsInitializer before doing any CameraUpdateFactory calls
        //try {
        MapsInitializer.initialize(this.getActivity());
        //} catch (GooglePlayServicesNotAvailableException e) {
        //    e.printStackTrace();
        //}

        // Updates the location and zoom of the MapView
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(37.2303918, -80.4218075), 15);
        map.animateCamera(cameraUpdate);

        name = (EditText)v.findViewById(R.id.location_title_input);
        description = (EditText)v.findViewById(R.id.location_description_input);
        cancel = (Button)v.findViewById(R.id.cancel_new_location);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clear();
            }
        });
        submit = (Button)v.findViewById(R.id.submit_new_location);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (location == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Tap the map at the top of the screen to select a location to submit as a nap.")
                            .setTitle("No Location Selected");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                if (name.getText().toString().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please provide the name of your nap location.")
                            .setTitle("No Name Provided");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                if (description.getText().toString().length() == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Please provide a short description of your nap location.")
                            .setTitle("No Description Provided");
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                    return;
                }
                ph.submitLocation(location, name.getText().toString(), description.getText().toString());
                clear();
            }
        });

        return v;
    }

    private void clear() {
        map.clear();
        location = null;
        name.setText("");
        description.setText("");
    }

    @Override
    public void onPause() {
        super.onPause();
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
        Toast.makeText(v.getContext(), "New Nap Submitted!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void commentsComplete() {
        //nothing
    }

    @Override
    public void completeFullness(int full) {
        //nothing here
    }
}
