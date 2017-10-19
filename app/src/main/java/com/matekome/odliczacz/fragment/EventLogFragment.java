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
import com.matekome.odliczacz.adapter.EventLogAdapter;
import com.matekome.odliczacz.data.db.EventDao;
import com.matekome.odliczacz.data.pojo.EventLog;
import com.matekome.odliczacz.data.realm.EventLogRealm;

import io.realm.RealmList;

public class EventLogFragment extends ListFragment {

    EventLogAdapter adapter;
    int eventId;
    EventDao dao;
    OnEventLogSelectedListener mCallback;

    public interface OnEventLogSelectedListener {
        void onEventLogSelected(EventLog eventLog);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            mCallback = (OnEventLogSelectedListener) context;
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
        View view = inflater.inflate(R.layout.fragment_event_log, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        RealmList<EventLogRealm> eventLogs = dao.getEventLogsByEventId(eventId);
        EventDetailFragment fragment = (EventDetailFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_event_detail);

        adapter = new EventLogAdapter(getContext(), eventLogs, fragment);
        setListAdapter(adapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        EventLogRealm eventLogRealm = adapter.getItem(position);
        EventLog eventLog = new EventLog(eventLogRealm.getDate());
        eventLog.setDescription(eventLogRealm.getDescription());

        mCallback.onEventLogSelected(eventLog);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.close();
    }
}
