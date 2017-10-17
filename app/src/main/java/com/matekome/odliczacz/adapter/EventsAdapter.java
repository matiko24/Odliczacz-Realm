package com.matekome.odliczacz.adapter;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.data.MyPeriod;
import com.matekome.odliczacz.data.realm.EventRealm;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

public class EventsAdapter extends RealmBaseAdapter<EventRealm> {

    static class ViewHolder {
        @BindView(R.id.event_name_text_view)
        TextView eventName;
        @BindView(R.id.difference_between_today_and_event_occurrence_text_view)
        TextView eventDate;

        private ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    public EventsAdapter(@Nullable RealmResults<EventRealm> data) {
        super(data);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_event, viewGroup, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (adapterData != null) {
            EventRealm event = adapterData.get(position);
            viewHolder.eventName.setText(event.getName());
            viewHolder.eventDate.setText(MyPeriod.getPeriodToDisplay(event.getEventOccurrences().first().getDate()));
        }

        return view;
    }
}
