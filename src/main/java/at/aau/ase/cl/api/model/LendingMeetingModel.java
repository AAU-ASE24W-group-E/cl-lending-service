package at.aau.ase.cl.api.model;

import java.time.Instant;

public class LendingMeetingModel {
    private Instant meetingTime;
    private String meetingLocation;
    private Instant deadline;

    public LendingMeetingModel(Instant meetingTime,
                               String meetingLocation,
                               Instant deadline) {
        this.meetingTime = meetingTime;
        this.deadline = deadline;
        this.meetingLocation = meetingLocation;
    }

    public LendingMeetingModel() {

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

    public String getMeetingLocation() {
        return meetingLocation;
    }

    public void setMeetingLocation(String meetingLocation) {
        this.meetingLocation = meetingLocation;
    }
}
