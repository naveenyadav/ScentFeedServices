package com.scent.feedservice.data.profile;

import java.util.Date;
import java.util.Objects;

public class Reportee {
    private String userId;
    private Date reportedOn;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getReportedOn() {
        return reportedOn;
    }

    public void setReportedOn(Date reportedOn) {
        this.reportedOn = reportedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reportee reportee = (Reportee) o;
        return Objects.equals(getUserId(), reportee.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUserId());
    }
}
