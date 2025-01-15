package at.aau.ase.cl.api.model;
import java.time.LocalDateTime;

import java.util.UUID;

public class LendingMeetingModel {
    private UUID id;
    private UUID lendingRequestId;
    private LocalDateTime meetingTime;
    private String meetingPlace;
    private LocalDateTime deadline;
    private LocalDateTime createdAt;

    public LendingMeetingModel() {
    }

    public LendingMeetingModel(UUID id, UUID lendingRequestId, LocalDateTime meetingTime,
                               String meetingPlace, LocalDateTime deadline, LocalDateTime createdAt) {
        this.id = id;
        this.lendingRequestId = lendingRequestId;
        this.meetingTime = meetingTime;
        this.meetingPlace = meetingPlace;
        this.deadline = deadline;
        this.createdAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public LocalDateTime getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(LocalDateTime meetingTime) {
        this.meetingTime = meetingTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getMeetingPlace() {
        return meetingPlace;
    }

    public void setMeetingPlace(String meetingPlace) {
        this.meetingPlace = meetingPlace;
    }

    public LocalDateTime getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline) {
        this.deadline = deadline;
    }


    public UUID getLendingRequestId() {
        return lendingRequestId;
    }

    public void setLendingRequestId(UUID lendingRequestId) {
        this.lendingRequestId = lendingRequestId;
    }
}
