package com.matekome.odliczacz.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.data.pojo.EventLog;
import com.matekome.odliczacz.fragment.EventDetailFragment;
import com.matekome.odliczacz.fragment.EventLogFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailActivity extends AppCompatActivity implements EventLogFragment.OnEventLogSelectedListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    EventDetailFragment eventDetailFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        eventDetailFragment = (EventDetailFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_event_detail);
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
    public void onEventLogSelected(EventLog eventLog) {
        eventDetailFragment.setEventLogDetails(eventLog);
    }
}
