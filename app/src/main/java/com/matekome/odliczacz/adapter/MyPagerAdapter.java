package com.matekome.odliczacz.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.matekome.odliczacz.fragment.EventsFragment;
import com.matekome.odliczacz.fragment.LoginFragment;

import java.util.ArrayList;
import java.util.List;

public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private static int NUM_OF_TABS = 2;
    private List<Fragment> fragmentList;

    public MyPagerAdapter(FragmentManager fm, boolean isLogin) {
        super(fm);
        fragmentList = new ArrayList<>();
        fragmentList.add(new EventsFragment(false));
        if (isLogin)
            fragmentList.add(new EventsFragment(true));
        else
            fragmentList.add(new LoginFragment());
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Publiczne";
            case 1:
                return "Prywatne";
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        if (object instanceof LoginFragment && fragmentList.get(1) instanceof EventsFragment)
            return POSITION_NONE;
        else
            return POSITION_UNCHANGED;
    }

    @Override
    public int getCount() {
        return NUM_OF_TABS;
    }

    public void replaceFragment() {
        fragmentList.remove(1);
        fragmentList.add(new EventsFragment(true));
        notifyDataSetChanged();
    }

}
