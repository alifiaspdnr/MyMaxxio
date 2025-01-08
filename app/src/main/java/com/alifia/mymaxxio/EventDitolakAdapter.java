package com.alifia.mymaxxio;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alifia.mymaxxio.model.Event;
import com.alifia.mymaxxio.model.EventItem;
import com.alifia.mymaxxio.model.ListItem;
import com.alifia.mymaxxio.model.TitleItem;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class EventDitolakAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ListItem> itemList;
    private FirebaseFirestore store;

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        public TextView dayOfWeek;
        public TextView date;
        public TextView eventName;
        public TextView eventDescription;
        public TextView tingkatan;

        public EventViewHolder(View v) {
            super(v);
            dayOfWeek = v.findViewById(R.id.tvDayOfWeek);
            date = v.findViewById(R.id.tvDate);
            eventName = v.findViewById(R.id.tvEventName);
            eventDescription = v.findViewById(R.id.tvEventDescription);
            tingkatan = v.findViewById(R.id.tingkatan);
        }
    }

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public TitleViewHolder(View v) {
            super(v);
            title = v.findViewById(R.id.tvEventMonthAndYear);
        }
    }

    public EventDitolakAdapter(List<ListItem> itemList) {
        Log.d("TAG", itemList.toString());
        this.itemList = itemList;
        store = FirebaseFirestore.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ListItem.TYPE_TITLE) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_event_title, parent, false);
            return new TitleViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_event_ditolak, parent, false);
            return new EventViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        switch (viewHolder.getItemViewType()) {
            case ListItem.TYPE_TITLE:
                String date = ((TitleItem) itemList.get(position)).getDate();
                TitleViewHolder titleHolder = (TitleViewHolder) viewHolder;

                titleHolder.title.setText(date);
                break;
            case ListItem.TYPE_EVENT:
                Event event = ((EventItem) itemList.get(position)).getEvent();
                EventViewHolder eventHolder = (EventViewHolder) viewHolder;

                Instant instant = event.getTgl().toInstant();
                ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
                String dayOfWeekName = zonedDateTime.getDayOfWeek().getDisplayName(TextStyle.SHORT, Locale.forLanguageTag("ID"));
                int dayOfMonth = zonedDateTime.getDayOfMonth();

                eventHolder.dayOfWeek.setText(dayOfWeekName);
                eventHolder.date.setText(String.valueOf(dayOfMonth));
                eventHolder.eventName.setText(event.getNamaKegiatan());
                eventHolder.eventDescription.setText(event.getDeskripsi());
                eventHolder.tingkatan.setText(event.getNamaTingkatan());

                eventHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Event event = ((EventItem) itemList.get(position)).getEvent();
                        Intent intent = new Intent(view.getContext(), EventDetailDitolakActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("eventId", event.getId().toString());
                        intent.putExtras(bundle);
                        view.getContext().startActivity(intent);
                    }
                });

                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }
}
