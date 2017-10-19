package com.matekome.odliczacz.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.fragment.LoginFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SetPasswordActivity extends AppCompatActivity implements LoginFragment.OnLoginListener {
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
    }

    @Override
    public void logIn(String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("odliczacz.preferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("firstRun", false);
        editor.putString("password", password);
        editor.commit();
        finish();
    }
}
