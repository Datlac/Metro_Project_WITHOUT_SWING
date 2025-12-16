package metro.models.operation;
import java.time.LocalDate;

import metro.enums.IncidentSeverity;
import metro.enums.IncidentStatus;

public class IncidentReport {
    private String reportId;
    private String title;
    private IncidentSeverity severity;
    private LocalDate reportedTime;
    private String affectedEquipmentId;
    private String description;
    private IncidentStatus status;

    public IncidentReport(String reportId, String title, IncidentSeverity severity) {
        this.reportId = reportId;
        this.title = title;
        this.severity = severity;
        this.reportedTime = LocalDate.now();
        this.status = IncidentStatus.NEW;
    }

    public void updateStatus(IncidentStatus newStatus, String note) {
        this.status = newStatus;
        System.out.println("Sự cố " + reportId + " đã chuyển sang trạng thái: " + newStatus);
    }
}