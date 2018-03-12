package com.app.ssoft.vrs.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Shekahar.Shrivastava on 12-Mar-18.
 */

public class Pager extends FragmentStatePagerAdapter {

    //integer to count number of tabs
    int tabCount;

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //Initializing tab count
        this.tabCount= tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                PastRideFragment pastRideFragment = new PastRideFragment();
                return pastRideFragment;
            case 1:
                UpcomingRideFragment upcomingRideFragment = new UpcomingRideFragment();
                return upcomingRideFragment;
            case 2:
                CurrentRideFragment currentRideFragment = new CurrentRideFragment();
                return currentRideFragment;
            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }
}