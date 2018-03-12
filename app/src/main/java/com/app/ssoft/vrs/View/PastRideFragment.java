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

public class PastRideFragment extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.past_ride_layout, container, false);
    }
}
