package com.matekome.odliczacz.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.data.db.EventDao;
import com.matekome.odliczacz.data.pojo.Event;
import com.matekome.odliczacz.data.pojo.EventLog;

import org.joda.time.DateTime;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewEventFragment extends Fragment {

    @BindView(R.id.fragment_new_event_edtv_new_event_name)
    EditText newEventNameEditText;
    @BindView(R.id.fragment_new_event_chb_is_now_event)
    CheckBox isNowEventCheckBox;
    @BindView(R.id.fragment_new_event_chb_is_private_event)
    CheckBox isPrivateEventCheckBox;
    @BindView(R.id.fragment_new_event_dp_event_log_date)
    DatePicker datePicker;
    @BindView(R.id.fragment_new_event_tp_event_log_time)
    TimePicker timePicker;
    @BindView(R.id.fragment_new_event_fab_add_event)
    FloatingActionButton addButton;
    boolean lastPageWasPrivateEventsList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lastPageWasPrivateEventsList = getActivity().getIntent().getExtras().getBoolean("lastPageWasPrivateEventsList");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_event, container, false);
        ButterKnife.bind(this, view);
        if (lastPageWasPrivateEventsList)
            isPrivateEventCheckBox.setChecked(true);
        timePicker.setIs24HourView(true);
        datePicker.setVisibility(View.GONE);
        timePicker.setVisibility(View.GONE);
        showKeyboard();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        isNowEventCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEventNameEditText.clearFocus();
                hideKeyboard();
            }
        });

        isPrivateEventCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newEventNameEditText.clearFocus();
                hideKeyboard();
            }
        });

        isNowEventCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    datePicker.setVisibility(View.GONE);
                    timePicker.setVisibility(View.GONE);
                } else {
                    datePicker.setVisibility(View.VISIBLE);
                    timePicker.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick(R.id.fragment_new_event_fab_add_event)
    public void addEvent() {
        String newEventName = newEventNameEditText.getText().toString();
        EventDao dao = new EventDao();

        if (TextUtils.isEmpty(newEventName)) {
            Toast.makeText(getContext(), getString(R.string.toast_event_without_name), Toast.LENGTH_SHORT).show();
            newEventNameEditText.requestFocus();
            showKeyboard();
        } else if (dao.isSuchNameEventExist(newEventName)) {
            Toast.makeText(getContext(), getString(R.string.toast_event_with_name_which_exists), Toast.LENGTH_SHORT).show();
            newEventNameEditText.requestFocus();
            showKeyboard();
        } else {
            Event newEvent = new Event();
            newEvent.setName(newEventName);
            newEvent.setPrivate(isPrivateEventCheckBox.isChecked());

            if (isNowEventCheckBox.isChecked()) {
                newEvent.getEventLogs().add(new EventLog(new Date()));
            } else {
                int selectedHour, selectedMinute;

                if (Build.VERSION.SDK_INT >= 23) {
                    selectedHour = timePicker.getHour();
                    selectedMinute = timePicker.getMinute();
                } else {
                    selectedHour = timePicker.getCurrentHour();
                    selectedMinute = timePicker.getCurrentMinute();
                }
                DateTime userSetDate = new DateTime(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(), selectedHour, selectedMinute);
                newEvent.getEventLogs().add(new EventLog(userSetDate.toDate()));
            }
            dao.insertNewEvent(newEvent);
            hideKeyboard();
            getActivity().finish();
        }
    }

    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);

    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }
}
