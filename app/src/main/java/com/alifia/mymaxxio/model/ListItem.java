package com.alifia.mymaxxio.model;

public abstract class ListItem {
    public static final int TYPE_TITLE = 0;
    public static final int TYPE_EVENT = 1;

    abstract public int getType();
}
