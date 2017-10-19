package com.matekome.odliczacz.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.fragment.EventsListFragment;
import com.matekome.odliczacz.fragment.LoginFragment;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private static int NUM_OF_TABS = 2;
    private List<Fragment> fragments;
    private String[] tabsNames;

    public MyPagerAdapter(FragmentManager fm, Context context, boolean isLogged) {
        super(fm);
        this.context = context;
        tabsNames = context.getResources().getStringArray(R.array.view_pager_tabs_names);

        fragments = new ArrayList<>();
        fragments.add(new EventsListFragment(false));
        if (isLogged)
            fragments.add(new EventsListFragment(true));
        else
            fragments.add(new LoginFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabsNames[position];
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof LoginFragment && fragments.get(1) instanceof EventsListFragment)
            return POSITION_NONE;
        else
            return POSITION_UNCHANGED;
    }

    @Override
    public int getCount() {
        return NUM_OF_TABS;
    }

    public void replaceFragment() {
        fragments.remove(1);
        fragments.add(new EventsListFragment(true));
        notifyDataSetChanged();
    }

}
