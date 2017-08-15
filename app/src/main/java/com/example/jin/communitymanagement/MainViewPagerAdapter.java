package com.example.jin.communitymanagement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by summe on 2017/8/15.
 */

public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments = new ArrayList<>();

    public MainViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);

    }

    @Override
    public int getCount() {
        return fragments.size();

    }
    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }

}
