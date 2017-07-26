package com.abyxcz.application.androidexamples;

/**
 * Created by Tim on 6/14/2017.
 */

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * A simple pager adapter that represents Page objects, in
 * sequence.
 */
class Adapter extends FragmentStatePagerAdapter {

    private int NUM_PAGES = 5;

    public Adapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        //switch on position for different types of pages
        switch(position){
            default: return new PageX();

            case 0: return new Page();
        }
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    public void setNumPages(int i){
        NUM_PAGES = i;
    }
}