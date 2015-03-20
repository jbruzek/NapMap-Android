package com.bruzek.jbruzek.myapplication;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.view.WindowManager;

import com.parse.Parse;


public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NapLocationsFragment locationsFrag;
    private MapFragment mapFrag;
    private AddLocationFragment addFrag;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        //set up the Nav Drawer
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //commit the map fragment
        if (getFragmentManager().findFragmentById(R.layout.map_fragment) == null) {
            mapFrag = new MapFragment();
        }
        getFragmentManager().beginTransaction()
                .add(R.id.container, mapFrag)
                .commit();
    }

    /**
     * this changes the fragment that is displayed currently based on when an item in the
     * nav drawer is selected
     * @param position
     */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fm = getFragmentManager();
        switch(position) {
            case 0:
                if (fm.findFragmentById(R.layout.map_fragment) == null) {
                    mapFrag = new MapFragment();
                }
                FragmentTransaction ft1 = fm.beginTransaction();
                ft1.replace(R.id.container, mapFrag)
                    .commit();
                mTitle = getString(R.string.map);
                break;
            case 1:
                if (fm.findFragmentById(R.layout.locations_fragment) == null) {
                    locationsFrag = new NapLocationsFragment();
                }
                FragmentTransaction ft2 = fm.beginTransaction();
                ft2.replace(R.id.container, locationsFrag)
                        .commit();
                mTitle = getString(R.string.nap_locations);
                break;
        }

    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.map);
                break;
            case 2:
                mTitle = getString(R.string.nap_locations);
                break;
            case 3:
                mTitle = getString(R.string.submit);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        getActionBar().setDisplayShowHomeEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("NapMap");
            adb.setMessage("NapMap was built as a part of VT Hacks 2015 by Joe and Adam. Logos and Icons by Katie and Sam.\n\nVersion 1.01\n2/9/2015");
            adb.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    //nothing????
                }
            });
            adb.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
