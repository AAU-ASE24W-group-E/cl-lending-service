package at.aau.ase.cl.api.model;

import java.time.Instant;
import java.util.UUID;

public class LendingModel {
    private UUID id;

    private UUID bookId;

    private UUID readerId;

    private UUID ownerId;

    private LendingStatus status;

    private Instant createdAt;

    private Instant updatedAt;

    public LendingModel() {
    }

    public LendingModel(UUID bookId,
                        UUID readerId,
                        UUID ownerId,
                        LendingStatus status) {
        this.bookId = bookId;
        this.readerId = readerId;
        this.ownerId = ownerId;
        this.status = status;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public UUID getReaderId() {
        return readerId;
    }

    public void setReaderId(UUID readerId) {
        this.readerId = readerId;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UUID ownerId) {
        this.ownerId = ownerId;
    }

    public LendingStatus getStatus() {
        return status;
    }

    public void setStatus(LendingStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
