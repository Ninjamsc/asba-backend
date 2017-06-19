package com.technoserv.dto;

import com.google.common.base.MoreObjects;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 *
 */
public class Notification {

    private Timestamp timestamp;

    private long skudResultId;

    private String note;

    public Notification(Timestamp timestamp, long skudResultId, String note) {
        this.timestamp = timestamp;
        this.skudResultId = skudResultId;
        this.note = note;
    }

    public Notification(long skudResultId, String note) {
        this(new Timestamp(Calendar.getInstance().getTimeInMillis()), skudResultId, note);
    }

    public Notification() {
        this(0, "");
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public long getSkudResultId() {
        return skudResultId;
    }

    public void setSkudResultId(long skudResultId) {
        this.skudResultId = skudResultId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Notification that = (Notification) o;

        if (skudResultId != that.skudResultId) return false;
        if (!timestamp.equals(that.timestamp)) return false;
        return note.equals(that.note);
    }

    @Override
    public int hashCode() {
        int result = timestamp.hashCode();
        result = 31 * result + (int) (skudResultId ^ (skudResultId >>> 32));
        result = 31 * result + note.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("timestamp", timestamp)
                .add("skudResultId", skudResultId)
                .add("note", note)
                .toString();
    }
}
