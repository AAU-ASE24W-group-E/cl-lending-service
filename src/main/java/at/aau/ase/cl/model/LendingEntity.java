package at.aau.ase.cl.model;

import at.aau.ase.cl.api.model.LendingStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "lending_requests")
public class LendingEntity extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, name = "book_id")
    private UUID bookId;

    @Column(nullable = false, name = "reader_id")
    private UUID readerId;

    @Column(nullable = false, name = "owner_id")
    private UUID ownerId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LendingStatus status;

    @Column(nullable = false, updatable = false, name = "created_at")
    private LocalDateTime createdAt;

    @Column(nullable = false, name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }
}
