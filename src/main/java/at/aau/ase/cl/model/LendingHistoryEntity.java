package at.aau.ase.cl.model;

import at.aau.ase.cl.api.model.LendingStatus;
import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "lending_history")
public class LendingHistoryEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false, name = "lending_request_id")
    private UUID lendingRequestId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LendingStatus status;

    @Column(nullable = false, updatable = false, name = "created_at")
    private Instant changedAt;

    public LendingHistoryEntity() {
        //
    }

    public UUID getId() {
        return id;
    }

    public UUID getLendingRequestId() {
        return lendingRequestId;
    }

    public void setLendingRequestId(UUID lendingRequestId) {
        this.lendingRequestId = lendingRequestId;
    }

    public LendingStatus getStatus() {
        return status;
    }

    public void setStatus(LendingStatus status) {
        this.status = status;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }
}