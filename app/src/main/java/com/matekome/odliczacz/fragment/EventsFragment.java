package com.matekome.odliczacz.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.activity.EventDetailActivity;
import com.matekome.odliczacz.adapter.EventsAdapter;
import com.matekome.odliczacz.data.db.EventDao;
import com.matekome.odliczacz.data.db.EventMapper;
import com.matekome.odliczacz.data.pojo.Event;
import com.matekome.odliczacz.data.pojo.EventOccurrence;
import com.matekome.odliczacz.data.realm.EventRealm;

import org.joda.time.DateTime;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class EventsFragment extends ListFragment {
    EventsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);

        Realm.init(getContext());
        RealmConfiguration realmConfig = new RealmConfiguration.Builder().name("odliczacz.realm").build();
        //Realm.deleteRealm(realmConfig);
        Realm.init(getContext());
        Realm.setDefaultConfiguration(realmConfig);

        EventDao dao = new EventDao();
        adapter = new EventsAdapter(dao.getAllEvents());
        setListAdapter(adapter);

        return view;
    }

    public void refreshEvents() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                EventDao dao = new EventDao();
                EventMapper mapper = new EventMapper();
                Event event = mapper.fromRealm(adapter.getItem(position));
                dao.addEventOccurrence(event, getCurrentData());

                Toast.makeText(getActivity(), getString(R.string.toast_reset_date), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                EventRealm eventRealm = (EventRealm) getListAdapter().getItem(position);
                EventMapper mapper = new EventMapper();
                Event event = mapper.fromRealm(eventRealm);
                intent.putExtra("eventId", event.getId());
                startActivity(intent);
            }
        });

    }

    public void showAddNewEventInputDialogTo() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_add_event, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        final CheckBox isNowEventCheckBox = (CheckBox) promptView.findViewById(R.id.is_now_event_check_box);
        final DatePicker datePicker = (DatePicker) promptView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) promptView.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        datePicker.setVisibility(View.GONE);
        timePicker.setVisibility(View.GONE);

        isNowEventCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    datePicker.setVisibility(View.GONE);
                    timePicker.setVisibility(View.GONE);
                } else {
                    datePicker.setVisibility(View.VISIBLE);
                    timePicker.setVisibility(View.VISIBLE);
                }
            }
        });

        final EditText newEventNameEditText = (EditText) promptView.findViewById(R.id.new_event_name);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String newEventNameString = newEventNameEditText.getText().toString();
                EventDao dao = new EventDao();

                if (TextUtils.isEmpty(newEventNameString)) {
                    Toast.makeText(getContext(), getString(R.string.toast_event_without_name), Toast.LENGTH_SHORT).show();
                } else if (dao.isSuchNameEventExist(newEventNameString)) {
                    Toast.makeText(getContext(), getString(R.string.toast_event_with_name_which_exists), Toast.LENGTH_SHORT).show();
                } else {
                    Event newEvent = new Event();
                    newEvent.setName(newEventNameString);

                    if (isNowEventCheckBox.isChecked()) {
                        newEvent.getEventOccurrences().add(new EventOccurrence(getCurrentData()));
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
                        newEvent.getEventOccurrences().add(new EventOccurrence(userSetDate.toString()));
                    }
                    dao.insertNewEvent(newEvent);
                    refreshEvents();
                }
            }

        });
        alertDialogBuilder.setNegativeButton(getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private String getCurrentData() {
        return new DateTime().toString();
    }

}
