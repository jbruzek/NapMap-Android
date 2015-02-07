package com.example.jbruzek.myapplication;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jbruzek on 2/7/15.
 */
public class MapFragment extends Fragment {

    public MapFragment() {
        //nothing
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.map_fragment, container, false);
        container.removeAllViews();
        return v;
    }
}
