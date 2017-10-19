package com.matekome.odliczacz.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.activity.EventDetailActivity;
import com.matekome.odliczacz.adapter.EventsAdapter;
import com.matekome.odliczacz.data.db.EventDao;
import com.matekome.odliczacz.data.db.EventMapper;
import com.matekome.odliczacz.data.pojo.Event;
import com.matekome.odliczacz.data.realm.EventRealm;

import java.util.Date;

@SuppressLint("ValidFragment")
public class EventsListFragment extends ListFragment {
    EventsAdapter adapter;
    EventDao dao = new EventDao();
    boolean showPrivateEvents;

    public EventsListFragment() {
    }

    public EventsListFragment(boolean showPrivateEvents) {
        this.showPrivateEvents = showPrivateEvents;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events_list, container, false);
        adapter = new EventsAdapter(dao.getAllEvents(showPrivateEvents));
        setListAdapter(adapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                EventMapper mapper = new EventMapper();
                Event event = mapper.fromRealm(adapter.getItem(position));
                dao.addEventLog(event, getCurrentData());

                Toast.makeText(getActivity(), getString(R.string.toast_reset_date), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventRealm eventRealm = (EventRealm) getListAdapter().getItem(position);
                EventMapper mapper = new EventMapper();
                Event event = mapper.fromRealm(eventRealm);

                Intent intent = new Intent(getActivity(), EventDetailActivity.class);
                intent.putExtra("eventId", event.getId());
                startActivity(intent);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dao.close();
    }

    public void refreshEventsList() {
        getListView().invalidateViews();
    }

    private Date getCurrentData() {
        return new Date();
    }

}
