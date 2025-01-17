package at.aau.ase.cl.api.model;

import java.time.Instant;
import java.util.UUID;

public class LendingMeetingModel {
    private UUID id;
    private UUID lendingRequestId;
    private Instant meetingTime;
    private String meetingPlace;
    private Instant deadline;
    private Instant createdAt;

    public LendingMeetingModel() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public Instant getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(Instant meetingTime) {
        this.meetingTime = meetingTime;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public Instant getDeadline() {
        return deadline;
    }

    public void setDeadline(Instant deadline) {
        this.deadline = deadline;
    }


    public UUID getLendingRequestId() {
        return lendingRequestId;
    }

    public void setLendingRequestId(UUID lendingRequestId) {
        this.lendingRequestId = lendingRequestId;
    }
}
