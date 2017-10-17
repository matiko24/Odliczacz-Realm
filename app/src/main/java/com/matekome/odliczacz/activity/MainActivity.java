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
import com.matekome.odliczacz.fragment.EventsFragment;
import com.matekome.odliczacz.fragment.LoginFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoginFragment.OnLoginListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.pager)
    ViewPager pager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    MyPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("odliczacz.preferences", Context.MODE_PRIVATE);
        boolean firstRun = sharedPreferences.getBoolean("firstRun", true);

        if (firstRun) {
            Intent intent = new Intent(this, SetPasswordActivity.class);
            startActivity(intent);
        }

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        setTitle(getString(R.string.your_events));

        adapter = new MyPagerAdapter(getSupportFragmentManager(), false);
        pager.setAdapter(adapter);
        tabs.setupWithViewPager(pager);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("odliczacz.preferences", MODE_PRIVATE);
        boolean isLogin = sharedPreferences.getBoolean("isLogin", false);
        adapter = new MyPagerAdapter(getSupportFragmentManager(), isLogin);
        pager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        EventsFragment currentFragment = (EventsFragment) pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());

        switch (item.getItemId()) {
            case R.id.refresh:
                currentFragment.refreshEventsList();
                break;
            case R.id.add_event_fab:
                Intent intent = new Intent(this, NewEventActivity.class);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        SharedPreferences sharedPreferences = getSharedPreferences("odliczacz.preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLogin", false);
        editor.commit();
        moveTaskToBack(true);
    }

    @Override
    public void login() {
        adapter.replaceFragment();
        adapter.notifyDataSetChanged();
    }
}
