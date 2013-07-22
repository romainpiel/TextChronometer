package com.romainpiel.textchronometer;

import android.content.Context;


public class DateRenderer {

    public static final String SECONDS = "seconds";
    public static final String MINUTES = "minutes";
    public static final String HOURS = "hours";
    public static final String DAYS = "days";

    private final String ago, now, sec, secs, min, mins, hour, hours, day, days;

    public DateRenderer(Context ctx) {
        ago = ctx.getString(R.string.general_timeAgo_);
        now = ctx.getString(R.string.general_momentAgo);
        sec = ctx.getString(R.string.general_second);
        secs = ctx.getString(R.string.general_seconds);
        min = ctx.getString(R.string.general_minute);
        mins = ctx.getString(R.string.general_minutes);
        hour = ctx.getString(R.string.general_hour);
        hours = ctx.getString(R.string.general_hours);
        day = ctx.getString(R.string.general_day);
        days = ctx.getString(R.string.general_days);
    }

    /**
     * Converts a timestamp to how long ago syntax
     *
     * @param time The time in milliseconds
     * @return The formatted time
     */
    public TimeAgo timeAgo(double time) {

        TimeAgo result = null;

        Unit[] units = new Unit[]
                {
                        new Unit(SECONDS, sec, secs, 60, 1),
                        new Unit(MINUTES, min, mins, 3600, 60),
                        new Unit(HOURS, hour, hours, 86400, 3600),
                        new Unit(DAYS, day, days, 604800, 86400)
                };

        long currentTime = System.currentTimeMillis();
        int difference = (int) ((currentTime - time) / 1000);

        if (difference < 5) {
            return new TimeAgo(units[0], now);
        }

        String formattedDate = null;
        Unit lastUnit = null;
        for (Unit unit : units) {
            if (difference < unit.limit) {
                formattedDate = getFormattedDate(unit, difference);
                lastUnit = unit;
                break;
            }
        }

        if (formattedDate == null) {
            lastUnit = units[units.length - 1];
            formattedDate = getFormattedDate(lastUnit, difference);
        }

        return new TimeAgo(lastUnit, formattedDate);
    }

    private String getFormattedDate(Unit unit, int difference) {
        int newDiff = (int) Math.floor(difference / unit.inSeconds);
        String resultValue = newDiff + " " + (newDiff <= 1? unit.name : unit.pluralName);
        return String.format(ago, resultValue);
    }

    public class TimeAgo {
        Unit unit;
        String formattedDate;

        public TimeAgo(Unit unit, String formattedDate) {
            this.unit = unit;
            this.formattedDate = formattedDate;
        }
    }

    static class Unit {

        public String type;
        public String name, pluralName;
        public int limit;
        public int inSeconds;

        public Unit(String type, String name, String pluralName, int limit, int inSeconds) {
            this.type = type;
            this.name = name;
            this.pluralName = pluralName;
            this.limit = limit;
            this.inSeconds = inSeconds;
        }
    }
}
