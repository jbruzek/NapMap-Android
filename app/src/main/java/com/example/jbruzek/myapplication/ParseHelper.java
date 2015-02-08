package com.example.jbruzek.myapplication;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

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

    public void queryLocations() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("locations");
        query.setLimit(5);
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

    public ArrayList<Location> getLocations() {
        return locations;
    }

    public Location currentLocation() {
        return currentLocation;
    }

    public ArrayList<String> getComments() {
        return comments;
    }

}
