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

    /**
     * constructor
     */
    public ParseHelper(ParseCallbacks pc) {
        this.pc = pc;
        locations = new ArrayList<Location>();
    }

    public void queryLocations() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("locations");
        query.whereExists("name");
        query.whereExists("fullness");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> locList, ParseException e) {
                if (e == null) {
                    for(ParseObject p : locList) {
                        Location l = new Location(p.getString("name"), p.getInt("fullness"));
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

    public ArrayList<Location> getLocations() {
        return locations;
    }

}
