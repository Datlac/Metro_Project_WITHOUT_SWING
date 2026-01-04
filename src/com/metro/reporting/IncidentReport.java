package com.metro.reporting;

import com.metro.enums.IncidentSeverity;
import com.metro.enums.IncidentStatus;
import java.time.LocalDateTime;

public class IncidentReport {
    private String reportId;
    private String description;
    private IncidentSeverity severity;
    private IncidentStatus status;
    private LocalDateTime reportedTime;
    private String reporterId;

    public IncidentReport(String reportId, String description, IncidentSeverity severity, String reporterId) {
        this.reportId = reportId;
        this.description = description;
        this.severity = severity;
        this.reporterId = reporterId;
        
        // CẬP NHẬT: Sử dụng NEW thay vì REPORTED
        this.status = IncidentStatus.NEW;
        
        this.reportedTime = LocalDateTime.now();
    }
    
    public String getReportId() {
		return reportId;
	}

	public void setReportId(String reportId) {
		this.reportId = reportId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public IncidentSeverity getSeverity() {
		return severity;
	}

	public void setSeverity(IncidentSeverity severity) {
		this.severity = severity;
	}

	public IncidentStatus getStatus() {
		return status;
	}

	public void setStatus(IncidentStatus status) {
		this.status = status;
	}

	public LocalDateTime getReportedTime() {
		return reportedTime;
	}

	public void setReportedTime(LocalDateTime reportedTime) {
		this.reportedTime = reportedTime;
	}

	public String getReporterId() {
		return reporterId;
	}

	public void setReporterId(String reporterId) {
		this.reporterId = reporterId;
	}

	public void updateStatus(IncidentStatus newStatus) {
        this.status = newStatus;
        System.out.println("Incident " + reportId + " updated to: " + newStatus);
    }
    
    @Override
    public String toString() {
        return "Incident[" + reportId + "] Severity: " + severity + " - Status: " + status;
    }
}