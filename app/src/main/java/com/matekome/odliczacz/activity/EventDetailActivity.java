package com.matekome.odliczacz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.data.pojo.EventOccurrence;
import com.matekome.odliczacz.fragment.EventDetailFragment;
import com.matekome.odliczacz.fragment.EventOccurrenceFragment;

public class EventDetailActivity extends AppCompatActivity implements EventOccurrenceFragment.OnEventOccurrenceSelectedListener {

    EventDetailFragment eventDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        eventDetailFragment = (EventDetailFragment) getSupportFragmentManager().findFragmentById(R.id.event_detail_fragment);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add:
                eventDetailFragment.showAddEventOccurenceDialog();
                break;
            case R.id.settings:
                eventDetailFragment.showEditingEventNameDialog();
                break;
            case R.id.delete:
                eventDetailFragment.showDeleteEventDialog();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, EventsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onEventOccurrenceSelected(EventOccurrence eventOccurrence) {
        eventDetailFragment.setEventDetails(eventOccurrence);
    }
}
