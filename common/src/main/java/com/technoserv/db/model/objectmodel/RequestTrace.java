package com.technoserv.db.model.objectmodel;

import com.google.common.base.MoreObjects;
import com.technoserv.db.model.BaseEntity;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 *
 */
@Entity
@Table(name = "request_trace")
public class RequestTrace extends BaseEntity<Long> {

    private Long id;

    private Long requestId;

    private Request.Status status;

    private Timestamp statusChangedAt;

    private String comment;

    public RequestTrace(Long requestId, Request.Status status, String comment) {
        this.id = null;
        this.requestId = requestId;
        this.status = status;
        this.statusChangedAt = new Timestamp(Calendar.getInstance().getTimeInMillis());
        this.comment = comment;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "request_wfm_id", nullable = false)
    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    public Request.Status getStatus() {
        return status;
    }

    public void setStatus(Request.Status status) {
        this.status = status;
    }

    @Column(name = "status_changed_at", nullable = false)
    public Timestamp getStatusChangedAt() {
        return statusChangedAt;
    }

    public void setStatusChangedAt(Timestamp statusChangedAt) {
        this.statusChangedAt = statusChangedAt;
    }

    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        RequestTrace that = (RequestTrace) o;

        if (!id.equals(that.id)) return false;
        if (!requestId.equals(that.requestId)) return false;
        if (status != that.status) return false;
        if (!statusChangedAt.equals(that.statusChangedAt)) return false;
        return comment != null ? comment.equals(that.comment) : that.comment == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + requestId.hashCode();
        result = 31 * result + status.hashCode();
        result = 31 * result + statusChangedAt.hashCode();
        result = 31 * result + (comment != null ? comment.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("requestId", requestId)
                .add("status", status)
                .add("statusChangedAt", statusChangedAt)
                .add("comment", comment)
                .toString();
    }
}
