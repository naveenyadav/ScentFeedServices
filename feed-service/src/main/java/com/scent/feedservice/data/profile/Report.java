package com.scent.feedservice.data.profile;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;
@Document(collection = "Reports")
public class Report {
    @Id
    private String userId;
    private Set<Reportee> reportedBy;
    public Report(){
        reportedBy = new HashSet<>();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Set<Reportee> getReportedBy() {
        return reportedBy;
    }

    public void setReportedBy(Set<Reportee> reportedBy) {
        this.reportedBy = reportedBy;
    }
    public boolean addReport(Reportee reportee){
        return reportedBy.add(reportee);
    }
}
