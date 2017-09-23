package com.matekome.odliczacz.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.data.MyPeriod;
import com.matekome.odliczacz.data.realm.EventRealm;

import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

public class EventsAdapter extends RealmBaseAdapter<EventRealm> {
    public EventsAdapter(@Nullable OrderedRealmCollection data) {
        super(data);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_event, viewGroup, false);

        EventRealm event = adapterData.get(i);

        TextView eventName = (TextView) view.findViewById(R.id.event_name);
        TextView eventDate = (TextView) view.findViewById(R.id.event_elapsed_time);

        eventName.setText(event.getName());
        eventDate.setText(MyPeriod.getPeriodToDisplay(event.getEventOccurrences().last().getDate()));

        return view;
    }
}
