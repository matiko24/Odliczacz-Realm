package com.matekome.odliczacz.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.adapter.EventOccurrencesAdapter;
import com.matekome.odliczacz.data.db.EventDao;
import com.matekome.odliczacz.data.pojo.EventOccurrence;
import com.matekome.odliczacz.data.realm.EventOccurrenceRealm;

import io.realm.RealmList;

public class EventOccurrenceFragment extends ListFragment {

    EventOccurrencesAdapter adapter;
    int eventId;
    EventDao dao;
    OnEventOccurrenceSelectedListener mCallback;

    public interface OnEventOccurrenceSelectedListener {
        void onEventOccurrenceSelected(EventOccurrence eventOccurrence);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mCallback = (OnEventOccurrenceSelectedListener) context;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventId = getActivity().getIntent().getExtras().getInt("eventId");
        dao = new EventDao();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_occurances, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RealmList<EventOccurrenceRealm> eventOccurrences = dao.getEventOccurrencesByEventId(eventId);
        EventDetailFragment fragment = (EventDetailFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.event_detail_fragment);

        adapter = new EventOccurrencesAdapter(getContext(), eventOccurrences, fragment);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        EventOccurrenceRealm eventOccurrenceRealm = adapter.getItem(position);
        EventOccurrence eventOccurrence = new EventOccurrence(eventOccurrenceRealm.getDate());
        eventOccurrence.setDescription(eventOccurrenceRealm.getDescription());

        mCallback.onEventOccurrenceSelected(eventOccurrence);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}
