package at.aau.ase.cl.api.model;

import java.time.Instant;
import java.util.UUID;

public class LendingHistoryModel {
    private UUID id;
    private UUID lendingRequestId;
    private LendingStatus status;
    private Instant changedAt;

    public LendingHistoryModel() {
    }

    public LendingHistoryModel(UUID id,
                               UUID lendingRequestId,
                               LendingStatus status,
                               Instant changedAt) {
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

    public Instant getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }
}
