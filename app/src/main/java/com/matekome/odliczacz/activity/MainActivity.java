package com.matekome.odliczacz.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.adapter.MyPagerAdapter;
import com.matekome.odliczacz.fragment.EventsListFragment;
import com.matekome.odliczacz.fragment.LoginFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.activity_main_view_pager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    MyPagerAdapter myPagerAdapter;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("odliczacz.preferences", Context.MODE_PRIVATE);

        if (isFirstRun()) {
            Intent intent = new Intent(this, SetPasswordActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        setTitle(getString(R.string.your_events));

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), this, false);
        viewPager.setAdapter(myPagerAdapter);
        tabs.setupWithViewPager(viewPager);
    }

    private boolean isFirstRun() {
        return sharedPreferences.getBoolean("firstRun", true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean isLogin = sharedPreferences.getBoolean("isLogged", false);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager(), this, isLogin);
        viewPager.setAdapter(myPagerAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EventsListFragment currentFragment = (EventsListFragment) viewPager.getAdapter().instantiateItem(viewPager, viewPager.getCurrentItem());

        switch (item.getItemId()) {
            case R.id.refresh:
                currentFragment.refreshEventsList();
                break;
            case R.id.fragment_new_event_fab_add_event:
                Intent intent = new Intent(this, NewEventActivity.class);
                intent.putExtra("lastPageWasPrivateEventsList", isCurrentPagePrivateEventsList());
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isCurrentPagePrivateEventsList() {
        int currentPageId = viewPager.getCurrentItem();
        return currentPageId == 1;
    }

    @Override
    public void onBackPressed() {
        setLogged(false);
        moveTaskToBack(true);
    }

    private void setLogged(boolean isLogged) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogged", isLogged);
        editor.commit();
    }

    @Override
    public void logIn(String password) {
        setLogged(true);
        myPagerAdapter.replaceFragment();
    }
}
