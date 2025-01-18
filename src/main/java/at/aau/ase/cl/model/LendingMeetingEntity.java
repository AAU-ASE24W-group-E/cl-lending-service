package at.aau.ase.cl.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.Instant;

@Embeddable
public class LendingMeetingEntity extends PanacheEntityBase {
    @Column(nullable = true, name = "meeting_location")
    private String meetingLocation;

    @Column(nullable = true, name = "meeting_time")
    private Instant meetingTime;

    @Column(nullable = true, name = "deadline")
    private Instant deadline;

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }

    public Instant getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(Instant meetingTime) {
        this.meetingTime = meetingTime;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }
}
