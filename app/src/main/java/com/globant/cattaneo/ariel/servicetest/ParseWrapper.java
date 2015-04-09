package com.globant.cattaneo.ariel.servicetest;

/**
 * Created by ariel.cattaneo on 07/04/2015.
 */
public class ParseWrapper extends DatabaseWrapper {
    @Override
    public Object getObjectFromTable(String table) {
        // TODO: Well... act like if this was gotten from Parse
        if (table.equals("event")) {
            Event event = new Event();
            event.setTitle("Title for an event from... ahem, Parse, yeh...");
            return event;
        }

        return null;
    }
}
