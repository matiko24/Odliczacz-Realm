package com.matekome.odliczacz.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.fragment.NewEventFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewEventActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                NewEventFragment fragment = (NewEventFragment) getSupportFragmentManager().findFragmentById(R.id.new_event_fragment);
                fragment.hideKeyboard();
            }
        });

    }

}
