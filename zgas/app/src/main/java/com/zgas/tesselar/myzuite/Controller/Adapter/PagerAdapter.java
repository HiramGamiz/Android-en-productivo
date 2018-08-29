package com.zgas.tesselar.myzuite.Controller.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that provides the adapter to populate pages inside of a ViewPager.
 *
 * @author jarvizu on 24/10/2017
 * @version 2018.0.9
 * @see FragmentPagerAdapter
 */
public class PagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new ArrayList<Fragment>();
    }

    /**
     * Method for adding a new fragment to the ViewPager
     *
     * @param fragment Fragment to be added.
     */
    public void addFragment(Fragment fragment) {
        this.fragments.add(fragment);
    }

    /**
     * Method for getting the fragment's  position.
     *
     * @param position
     * @return The fragment's position.
     */
    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }
}
