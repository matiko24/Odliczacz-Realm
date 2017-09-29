package com.matekome.odliczacz.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.matekome.odliczacz.R;
import com.matekome.odliczacz.data.realm.EventOccurrenceRealm;
import com.matekome.odliczacz.fragment.EventDetailFragment;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmBaseAdapter;

public class EventOccurrencesAdapter extends RealmBaseAdapter<EventOccurrenceRealm> {
    private EventDetailFragment fragment;
    private Context context;

    private static class ViewHolder {
        TextView date;
        ImageButton delete;
    }

    public EventOccurrencesAdapter(Context context, @Nullable OrderedRealmCollection<EventOccurrenceRealm> data, EventDetailFragment fragment) {
        super(data);
        this.fragment = fragment;
        this.context = context;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.row_event_occurrence, viewGroup, false);
            viewHolder = new ViewHolder();
            viewHolder.date = (TextView) view.findViewById(R.id.event_history);
            viewHolder.delete = (ImageButton) view.findViewById(R.id.delete_button);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (adapterData != null) {
            final EventOccurrenceRealm eventOccurrence = adapterData.get(i);
            Date eventOccurrenceDate = eventOccurrence.getDate();
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("dd.MM.YYYY HH:mm");
            DateTime eventDate = new DateTime(eventOccurrenceDate);

            viewHolder.date.setText(eventDate.toString(dateTimeFormatter));

            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                    alertDialogBuilder.setMessage(context.getString(R.string.delete_confirmation)).setCancelable(false).setPositiveButton(context.getString(R.string.yes), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            Realm realm = Realm.getDefaultInstance();
                            realm.executeTransaction(new Realm.Transaction() {
                                @Override
                                public void execute(Realm realm) {
                                    adapterData.deleteFromRealm(i);
                                }
                            });
                            fragment.setLastEventOccurrenceValues();

                        }
                    }).setNegativeButton(context.getString(R.string.no), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            });

        }









        return view;
    }
}
