package com.alifia.mymaxxio.model;

public class EventItem extends ListItem {
    private Event event;

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    @Override
    public int getType() {
        return TYPE_EVENT;
    }
}