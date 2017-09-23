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
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.activity.EventDetailActivity;
import com.matekome.odliczacz.activity.EventsActivity;
import com.matekome.odliczacz.data.MyPeriod;
import com.matekome.odliczacz.data.db.EventDao;
import com.matekome.odliczacz.data.pojo.Event;
import com.matekome.odliczacz.data.pojo.EventOccurrence;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;
import org.joda.time.Seconds;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class EventDetailFragment extends Fragment {

    TextView tvEventDate;
    TextView tvEventName;
    TextView tvDifferenceBetweenTodayandEventOccurrence;
    TextView tvSinceOrToEvent;
    EditText etvEventDescription;
    Spinner spinnerUniteOfTime;
    ImageButton imbtSaveEventOccurenceDescription;
    Event event;
    EventOccurrence displayedEventOccurrence;
    EventDao dao = new EventDao();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_details, container, false);

        findViews(view);

        int eventId = getActivity().getIntent().getExtras().getInt("eventId");
        initializeEvent(eventId);

        imbtSaveEventOccurenceDescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newDescription = etvEventDescription.getText().toString();
                updateDisplayedEventOccurrenceDescription(newDescription);

                etvEventDescription.clearFocus();
                hideKeyboard();
            }
        });

        spinnerUniteOfTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDifferenceBetweenTodayAndEventOccurrence(displayedEventOccurrence.getDate());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        return view;
    }

    private void updateDisplayedEventOccurrenceDescription(String newDescription) {
        displayedEventOccurrence.setDescription(newDescription);
        dao.udpateEventOccurence(displayedEventOccurrence);
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

    private void deleteEvent() {
        //Todo: usunąć
        Intent intent = new Intent(getContext(), EventsActivity.class);
        startActivity(intent);
    }

    public void showEditingEventNameDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_edit_event_name, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        final EditText editingEventName = (EditText) promptView.findViewById(R.id.editing_event_name);

        alertDialogBuilder.setMessage(getString(R.string.change_event_name_information)).setCancelable(false).setPositiveButton(getString(R.string.save), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newEventName = editingEventName.getText().toString();

                if (newEventName.equals("")) {
                    Toast.makeText(getContext(), "Nie można zapisać wydarzenia bez nazwy", Toast.LENGTH_SHORT).show();
                } else if (dao.isSuchNameEventExist(newEventName)) {
                    Toast.makeText(getContext(), "Wydarzenie o takiej nazwie już istnieje", Toast.LENGTH_SHORT).show();
                } else {
                    tvEventName.setText(editingEventName.getText().toString());
                    Intent intent = new Intent(getContext(), EventDetailActivity.class);
                    intent.putExtra("eventId", editingEventName.getText().toString());
                    startActivity(intent);
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

    public void setDifferenceBetweenTodayAndEventOccurrence(String eventDate) {
        int position = spinnerUniteOfTime.getSelectedItemPosition();
        DateTime dtCurrentDate = new DateTime();
        DateTime dtEventDate = DateTime.parse(eventDate);
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

        tvDifferenceBetweenTodayandEventOccurrence.setText(textToSet);
    }

    private void initializeEvent(int eventId) {
        this.event = dao.getEventById(eventId);
        tvEventName.setText(event.getName());
        setLastEventOccurrenceValues();
    }

    public void setLastEventOccurrenceValues() {
        event = dao.getEventById(event.getId());
        EventOccurrence lastEventOccurrence = event.getEventOccurrences().get(event.getEventOccurrences().size() - 1);
        setEventDetails(lastEventOccurrence);
    }

    public void setEventDetails(EventOccurrence eventOccurrence) {
        displayedEventOccurrence = eventOccurrence;

        String occurrenceDate = eventOccurrence.getDate();
        String occurrenceDescription = eventOccurrence.getDescription();

        DateTime dtEventDate = DateTime.parse(occurrenceDate);
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd.MM.YYYY HH:mm");
        tvEventDate.setText(dtEventDate.toString(dateTimeFormatter));

        setDifferenceBetweenTodayAndEventOccurrence(eventOccurrence.getDate());

        if (occurrenceDescription != null)
            etvEventDescription.setText(occurrenceDescription);
        else
            etvEventDescription.setText(null);
    }

    public void showAddEventOccurenceDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View promptView = layoutInflater.inflate(R.layout.dialog_add_event_occurrence, null);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setView(promptView);

        final DatePicker datePicker = (DatePicker) promptView.findViewById(R.id.date_picker);
        final TimePicker timePicker = (TimePicker) promptView.findViewById(R.id.time_picker);
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
                dao.addEventOccurrence(event, userSetDate.toString());
                setLastEventOccurrenceValues();
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

    private void findViews(View view) {
        tvEventDate = (TextView) view.findViewById(R.id.event_date_text_view);
        tvEventName = (TextView) view.findViewById(R.id.event_name);
        tvDifferenceBetweenTodayandEventOccurrence = (TextView) view.findViewById(R.id.event_elapsed_time);
        tvSinceOrToEvent = (TextView) view.findViewById(R.id.sinceOrToString);
        spinnerUniteOfTime = (Spinner) view.findViewById(R.id.spinner);
        etvEventDescription = (EditText) view.findViewById(R.id.event_description);
        imbtSaveEventOccurenceDescription = (ImageButton) view.findViewById(R.id.save_description_button);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
