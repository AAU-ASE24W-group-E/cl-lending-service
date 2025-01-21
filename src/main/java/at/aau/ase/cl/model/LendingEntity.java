package at.aau.ase.cl.model;

import at.aau.ase.cl.api.interceptor.exceptions.IllegalStatusException;
import at.aau.ase.cl.api.model.LendingStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.logging.Log;
import jakarta.persistence.*;

import java.time.Instant;
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

    @Embedded
    private LendingMeetingEntity lendingMeeting;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LendingStatus status;

    @Column(nullable = false, updatable = false, name = "created_at")
    private Instant createdAt;

    @Column(nullable = false, name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    LendingStatus validateStatusTransition(LendingStatus newStatus) {
        Log.tracef("Attempt to change %s status from %s to %s", this.id, this.status, newStatus);

        return switch (this.status) {
            case null -> newStatus;

            case READER_CREATED_REQUEST -> switch (newStatus) {
                case OWNER_SUGGESTED_MEETING, OWNER_DENIED, READER_WITHDREW -> newStatus;
                default -> throw new IllegalStatusException("Invalid status transition");
            };

            case OWNER_SUGGESTED_MEETING -> switch (newStatus) {
                case READER_ACCEPTED_MEETING, READER_WITHDREW, OWNER_DENIED -> newStatus;
                default -> throw new IllegalStatusException("Invalid status transition");
            };

            case READER_ACCEPTED_MEETING -> switch (newStatus) {
                case READER_CONFIRMED_TRANSFER, OWNER_CONFIRMED_TRANSFER, READER_WITHDREW, OWNER_DENIED -> newStatus;
                default -> throw new IllegalStatusException("Invalid status transition");
            };

            case READER_CONFIRMED_TRANSFER, OWNER_CONFIRMED_TRANSFER -> switch (newStatus) {
                case OWNER_CONFIRMED_TRANSFER, READER_CONFIRMED_TRANSFER -> LendingStatus.BORROWED;
                default -> throw new IllegalStatusException("Invalid status transition");
            };

            case BORROWED -> switch (newStatus) {
                case READER_RETURNED_BOOK, OWNER_CONFIRMED_RETURNAL -> newStatus;
                default -> throw new IllegalStatusException("Invalid status transition");
            };

            case READER_RETURNED_BOOK, OWNER_CONFIRMED_RETURNAL -> switch (newStatus) {
                case OWNER_CONFIRMED_RETURNAL, READER_RETURNED_BOOK -> LendingStatus.OWNER_CONFIRMED_RETURNAL;
                case LENDING_COMPLETED -> LendingStatus.LENDING_COMPLETED;
                default -> throw new IllegalStatusException("Invalid status transition");
            };

            case READER_WITHDREW, OWNER_DENIED, LENDING_COMPLETED -> throw new IllegalStatusException("No further transitions allowed from this status");

            default -> throw new IllegalStatusException("Unknown status");
        };

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getCreatedAt() {
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
        this.status = validateStatusTransition(status);
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getBookId() {
        return bookId;
    }

    public void setBookId(UUID bookId) {
        this.bookId = bookId;
    }

    public LendingMeetingEntity getLendingMeeting() {
        return lendingMeeting;
    }

    public void setLendingMeeting(LendingMeetingEntity lendingMeeting) {
        this.lendingMeeting = lendingMeeting;
    }
}
