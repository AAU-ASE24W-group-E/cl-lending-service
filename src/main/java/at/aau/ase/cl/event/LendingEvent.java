package at.aau.ase.cl.event;

import at.aau.ase.cl.api.model.LendingStatus;

import java.util.UUID;

public record LendingEvent(
        UUID bookId,
        UUID ownerId,
        LendingStatus status
) {
}
