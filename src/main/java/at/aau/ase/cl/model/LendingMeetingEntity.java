package at.aau.ase.cl.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Embeddable
public class LendingMeetingEntity extends PanacheEntityBase {

    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
    // important TODO
    private UUID id;

    @Column(nullable = true, name = "meeting_location")
    private String meetingLocation;

    @Column(nullable = true, name = "meeting_time")
    private Instant meetingTime;

    @Column(nullable = true, name = "deadline")
    private Instant deadline;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

//    public LendingEntity getLending() {
//        return lending;
//    }
//
//    public void setLending(LendingEntity lending) {
//        this.lending = lending;
//    }

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
