package com.matekome.odliczacz.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.fragment.EventsFragment;

public class EventsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle(getString(R.string.your_events));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_events, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Todo: prawo demeter
        EventsFragment fragment = (EventsFragment) getSupportFragmentManager().findFragmentById(R.id.events_fragment);

        switch (item.getItemId()) {
            case R.id.refresh:
                fragment.refreshEvents();
                break;
            case R.id.add_event:
                fragment.showAddNewEventInputDialogTo();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

}
