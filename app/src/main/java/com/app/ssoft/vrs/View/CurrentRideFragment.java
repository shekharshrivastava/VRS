package com.app.ssoft.vrs.View;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.ssoft.vrs.R;

/**
 * Created by Shekahar.Shrivastava on 12-Mar-18.
 */

public class CurrentRideFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Returning the layout file after inflating
        //Change R.layout.tab1 in you classes
        return inflater.inflate(R.layout.current_layout, container, false);
    }
}
