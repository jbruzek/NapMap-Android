package com.bruzek.jbruzek.myapplication;

import android.app.Application;

import com.parse.Parse;

/**
 * Created by jbruzek on 2/9/15.
 */
public class NapMap extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //set up Parse
        /*
            Parse has to be called form a application class instead of an Activity because it causes
            network runtime exceptions if it is initialized in an activity because of the
            Activity lifecycle.
         */
        Parse.initialize(this, "ni4fDAUMR7GIrlUSN86APVmlrQt91a4UXO78tkyE", "lraQ7qAwiAfGylhV4wEWWtyLWmzhZweZnrjEZc1F");
    }
}
