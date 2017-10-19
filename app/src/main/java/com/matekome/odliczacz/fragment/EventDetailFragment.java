package com.matekome.odliczacz.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.activity.MainActivity;
import com.matekome.odliczacz.data.MyPeriod;
import com.matekome.odliczacz.data.db.EventDao;
import com.matekome.odliczacz.data.pojo.Event;
import com.matekome.odliczacz.data.pojo.EventLog;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EventDetailFragment extends Fragment {

    @BindView(R.id.fragment_event_details_tv_event_date)
    TextView tvEventDate;
    @BindView(R.id.fragment_event_details_tv_event_name)
    TextView tvEventName;
    @BindView(R.id.fragment_event_details_tv_difference_between_today_and_event_log)
    TextView tvDifferenceBetweenTodayandEventLog;
    @BindView(R.id.fragment_event_details_tv_since_or_to_message)
    TextView tvSinceOrToEvent;
    @BindView(R.id.fragment_event_details_edtv_event_description)
    EditText etvEventLogDescription;
    @BindView(R.id.fragment_event_details_spn_period)
    Spinner spinnerUniteOfTime;
    @BindView(R.id.fragment_event_details_btn_save_description)
    ImageButton imbtSaveEventOccurenceDescription;
    Event event;
    EventLog displayedEventLog;
    EventDao dao;
    int eventId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = getActivity().getIntent().getExtras().getInt("eventId");
        dao = new EventDao();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);
        ButterKnife.bind(this, view);
        imbtSaveEventOccurenceDescription.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initializeEvent(eventId);

        etvEventLogDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                imbtSaveEventOccurenceDescription.setVisibility(View.VISIBLE);
                return false;
            }
        });

        imbtSaveEventOccurenceDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDescription = etvEventLogDescription.getText().toString();
                updateEventLogDescription(newDescription);
                imbtSaveEventOccurenceDescription.setVisibility(View.GONE);
                hideKeyboard();
            }
        });

        spinnerUniteOfTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDifferenceBetweenTodayAndEventLog(displayedEventLog.getDate());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.close();
    }

    private void updateEventLogDescription(String newDescription) {
        displayedEventLog.setDescription(newDescription);
        dao.udpateEventOccurence(displayedEventLog);
    }

    public void showDeleteEventDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(getString(R.string.delete_confirmation)).setCancelable(false).setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteEvent();
            }
        }).setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void deleteEvent() {
        dao.deleteEventById(eventId);
        Intent intent = new Intent(getContext(), MainActivity.class);
        startActivity(intent);
    }

    //Todo: Osobno
    public void showEditingEventNameDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_edit_event, null);
        //Todo: ButterKnife
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editingEventName = (EditText) promptView.findViewById(R.id.dialog_edit_event_edtv_event_new_name);
        final CheckBox isPrivateEventCheckBox = (CheckBox) promptView.findViewById(R.id.fragment_new_event_chb_is_private_event);
        editingEventName.setHint(event.getName());
        isPrivateEventCheckBox.setChecked(event.isPrivate());

        alertDialogBuilder.setMessage(getString(R.string.change_event_name_information)).setCancelable(false).setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newEventName = editingEventName.getText().toString();

                if (newEventName.equals("")) {
                    event.setPrivate(isPrivateEventCheckBox.isChecked());
                    dao.updateEvent(event);
                } else if (dao.isSuchNameEventExist(newEventName)) {
                    Toast.makeText(getContext(), "Wydarzenie o takiej nazwie już istnieje", Toast.LENGTH_SHORT).show();
                } else {
                    tvEventName.setText(newEventName);
                    event.setName(newEventName);
                    event.setPrivate(isPrivateEventCheckBox.isChecked());
                    dao.updateEvent(event);
                }
            }
        }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }

        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void setDifferenceBetweenTodayAndEventLog(Date eventDate) {
        int position = spinnerUniteOfTime.getSelectedItemPosition();
        DateTime dtCurrentDate = new DateTime();
        DateTime dtEventDate = new DateTime(eventDate);
        String textToSet = "";
        switch (position) {
            case 0:
                textToSet = MyPeriod.getPeriodToDisplay(eventDate);
                break;
            case 1:
                textToSet = String.valueOf(Years.yearsBetween(dtEventDate, dtCurrentDate).getYears());
                break;
            case 2:
                textToSet = String.valueOf(Months.monthsBetween(dtEventDate, dtCurrentDate).getMonths());
                break;
            case 3:
                textToSet = String.valueOf(Days.daysBetween(dtEventDate, dtCurrentDate).getDays());
                break;
            case 4:
                textToSet = String.valueOf(Hours.hoursBetween(dtEventDate, dtCurrentDate).getHours());
                break;
            case 5:
                textToSet = String.valueOf(Minutes.minutesBetween(dtEventDate, dtCurrentDate).getMinutes());
                break;
            case 6:
                textToSet = String.valueOf(Seconds.secondsBetween(dtEventDate, dtCurrentDate).getSeconds());
                break;
        }
        if (textToSet.contains("-"))
            tvSinceOrToEvent.setText(getString(R.string.to_event));
        else
            tvSinceOrToEvent.setText(getString(R.string.since_event));

        tvDifferenceBetweenTodayandEventLog.setText(textToSet);
    }

    private void initializeEvent(int eventId) {
        this.event = dao.getEventById(eventId);
        tvEventName.setText(event.getName());
        setLastEventLogValues();
    }

    public void setLastEventLogValues() {
        event = dao.getEventById(event.getId());
        EventLog lastEventLog = event.getEventLogs().get(0);
        setEventLogDetails(lastEventLog);
    }

    public void setEventLogDetails(EventLog eventLog) {
        displayedEventLog = eventLog;

        Date eventLogDate = eventLog.getDate();
        String eventLogDescription = eventLog.getDescription();

        DateTime dtEventDate = new DateTime(eventLogDate);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd.MM.YYYY HH:mm");
        tvEventDate.setText(dtEventDate.toString(dateTimeFormatter));

        setDifferenceBetweenTodayAndEventLog(eventLog.getDate());

        if (eventLogDescription != null)
            etvEventLogDescription.setText(eventLogDescription);
        else
            etvEventLogDescription.setText(null);
    }

    //Todo: osobna klasa
    public void showAddEventOccurenceDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_add_event_log, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        final DatePicker datePicker = (DatePicker) promptView.findViewById(R.id.fragment_new_event_dp_event_log_date);
        final TimePicker timePicker = (TimePicker) promptView.findViewById(R.id.fragment_new_event_tp_event_log_time);
        timePicker.setIs24HourView(true);

        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                int selectedHour, selectedMinute;

                if (Build.VERSION.SDK_INT >= 23) {
                    selectedHour = timePicker.getHour();
                    selectedMinute = timePicker.getMinute();
                } else {
                    selectedHour = timePicker.getCurrentHour();
                    selectedMinute = timePicker.getCurrentMinute();
                }

                DateTime userSetDate = new DateTime(datePicker.getYear(), datePicker.getMonth() + 1, datePicker.getDayOfMonth(), selectedHour, selectedMinute);
                dao.addEventLog(event, userSetDate.toDate());
                setLastEventLogValues();
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

    private void hideKeyboard() {
        etvEventLogDescription.clearFocus();
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public Event getEvent() {
        return event;
    }
}
