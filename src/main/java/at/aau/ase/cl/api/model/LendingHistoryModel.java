package at.aau.ase.cl.api.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class LendingHistoryModel {
    private UUID id;
    private UUID lendingRequestId;
    private LendingStatus status;
    private LocalDateTime changedAt;

    public LendingHistoryModel() {
    }

    public LendingHistoryModel(UUID id, UUID lendingRequestId, LendingStatus status, LocalDateTime changedAt) {
        this.id = id;
        this.lendingRequestId = lendingRequestId;
        this.status = status;
        this.changedAt = changedAt;
    }

    public LendingStatus getStatus() {
        return status;
    }

    public void setStatus(LendingStatus status) {
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getLendingRequestId() {
        return lendingRequestId;
    }

    public void setLendingRequestId(UUID lendingRequestId) {
        this.lendingRequestId = lendingRequestId;
    }

    public LocalDateTime getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(LocalDateTime changedAt) {
        this.changedAt = changedAt;
    }
}
