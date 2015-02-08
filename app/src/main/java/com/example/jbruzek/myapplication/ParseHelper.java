package com.example.jbruzek.myapplication;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * ParseHelper handles all Parse interactions for this app
 *
 * Created by jbruzek on 2/7/15.
 */
public class ParseHelper {

    private ParseCallbacks pc;
    private ArrayList<Location> locations;
    private Location currentLocation;
    private ArrayList<String> comments;

    /**
     * constructor
     */
    public ParseHelper(ParseCallbacks pc) {
        this.pc = pc;
        locations = new ArrayList<Location>();
        comments = new ArrayList<String>();
    }

    public void queryLocations(int number) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("locations");
        query.setLimit(number);
        query.orderByDescending("rating");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> locList, ParseException e) {
                if (e == null) {
                    for(ParseObject p : locList) {
                        Location l = new Location(p.getString("name"), p.getInt("fullness"));
                        l.rating(p.getDouble("rating"));
                        l.longitude(p.getDouble("longitude"));
                        l.latitude(p.getDouble("latitude"));
                        l.description(p.getString("description"));
                        l.id(p.getObjectId());
                        locations.add(l);
                    }
                    Log.d("locations", "Retrieved " + locList.size() + " locations");
                    pc.complete();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void queryById(String id) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("locations");
        Log.d("query", "Searching for " + id);
        query.whereEqualTo("objectId", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> locList, ParseException e) {
                if (e == null) {
                    for(ParseObject p : locList) {
                        currentLocation = new Location();
                        currentLocation.title(p.getString("name"));
                        currentLocation.fullness(p.getInt("fullness"));
                        currentLocation.rating(p.getDouble("rating"));
                        currentLocation.longitude(p.getDouble("longitude"));
                        currentLocation.latitude(p.getDouble("latitude"));
                        currentLocation.description(p.getString("description"));
                        currentLocation.id(p.getObjectId());
                    }
                    pc.complete();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void queryComments(String id) {
        Log.d("QUERY", "id: " + id);
        ParseQuery<ParseObject> query = ParseQuery.getQuery("comments");
        query.setLimit(10);
        query.orderByAscending("createdAt");
        query.whereEqualTo("nap", id);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> locList, ParseException e) {
                if (e == null) {
                    for(ParseObject p : locList) {
                        comments.add(p.getString("comment"));
                    }
                    Log.d("Comments", "Retrieved " + locList.size() + " comments");
                    pc.commentsComplete();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    public void submitLocation(LatLng l, String name, String description) {
        ParseObject location = new ParseObject("locations");
        location.put("latitude", l.latitude);
        location.put("longitude", l.longitude);
        location.put("name", name);
        location.put("description", description);
        location.put("fullness", 0);
        location.put("rating", 0);
        location.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                pc.complete();
            }
        });
    }

    public void rateNap(final String napId, final int rating, final String deviceId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ratings");
        query.whereEqualTo("napid", napId);
        query.whereEqualTo("deviceid", deviceId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> locList, ParseException e) {
                if (e == null) {
                    if (locList.size() == 0) {
                        submitRating(napId, rating, deviceId);
                    }

                    pc.complete();
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
    }

    private void submitRating(final String napId, final int rating, final String deviceId) {
        final ArrayList<Integer> list = new ArrayList<Integer>();
        list.add(rating);

        ParseQuery<ParseObject> query = ParseQuery.getQuery("ratings");
        query.whereEqualTo("napid", napId);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> locList, ParseException e) {
                ArrayList<Integer> ratings = new ArrayList<Integer>();
                if (e == null) {
                    for(ParseObject p : locList) {
                        list.add(p.getInt("rating"));
                    }
                } else {
                    Log.d("score", "Error: " + e.getMessage());
                }
            }
        });
        final double finalRating = average(list);

        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("locations");
        query2.getInBackground(napId, new GetCallback<ParseObject>() {
            public void done(ParseObject location, ParseException e) {
                if (e == null) {
                    location.put("rating", finalRating);
                    location.saveInBackground();

                    ParseObject rat = new ParseObject("ratings");
                    rat.put("napid", napId);
                    rat.put("deviceid", deviceId);
                    rat.put("rating", rating);
                    rat.saveInBackground();
                }
            }
        });
    }

    public void submitComment(String comment, String napId) {
        ParseObject rat = new ParseObject("comments");
        rat.put("nap", napId);
        rat.put("comment", comment);
        rat.saveInBackground();
    }

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public Location currentLocation() {
        return currentLocation;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

    private double average(ArrayList<Integer> list) {
        double sum = 0.0;
        for (Integer i : list) {
            sum += (double)i;
        }
        return sum / list.size();
    }

}
