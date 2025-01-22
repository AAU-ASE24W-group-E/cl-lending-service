package at.aau.ase.cl.service;

import at.aau.ase.cl.api.interceptor.exceptions.LendingRequestExistsException;
import at.aau.ase.cl.api.interceptor.exceptions.NotFoundException;
import at.aau.ase.cl.api.model.LendingMeetingModel;
import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.api.model.LendingStatus;
import at.aau.ase.cl.mapper.LendingMapper;
import at.aau.ase.cl.mapper.LendingMeetingMapper;
import at.aau.ase.cl.model.LendingEntity;
import at.aau.ase.cl.model.LendingHistoryEntity;
import at.aau.ase.cl.model.LendingMeetingEntity;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class LendingService {
    @Inject
    EventService eventService;

    @Transactional
    public LendingModel createLending(LendingModel lendingModel) {
        boolean existingRequest = LendingEntity.find("bookId = ?1 and readerId = ?2 and status not in ?3",
                        lendingModel.getBookId(),
                        lendingModel.getReaderId(),
                        List.of(LendingStatus.LENDING_COMPLETED, LendingStatus.READER_WITHDREW, LendingStatus.OWNER_DENIED))
                .firstResultOptional()
                .isPresent();

        if (existingRequest) {
            throw new LendingRequestExistsException("A lending request for this book by the same reader already exists and is not completed or cancelled.");
        }

        LendingEntity lendingEntity = LendingMapper.INSTANCE.map(lendingModel);

        lendingEntity.persistAndFlush();

        eventService.sendLendingEvent(lendingEntity);

        return LendingMapper.INSTANCE.map(lendingEntity);
    }

    @Transactional
    public LendingModel createLendingMeeting(LendingMeetingModel lendingMeetingModel,
                                             UUID lendingId) {
        LendingMeetingEntity lendingMeetingEntity = LendingMeetingMapper.INSTANCE.map(lendingMeetingModel);

        LendingEntity lendingEntity = getLendingById(lendingId);
        lendingEntity.setLendingMeeting(lendingMeetingEntity);
        lendingEntity.setStatus(LendingStatus.OWNER_SUGGESTED_MEETING);

        LendingEntity.flush();

        eventService.sendLendingEvent(lendingEntity);

        return LendingMapper.INSTANCE.map(lendingEntity);
    }

    public LendingEntity getLendingById(UUID id) {
        LendingEntity lending = LendingEntity.findById(id);

        if (lending == null) {
            Log.debugf("Lending with id %s not found", id);
            throw new NotFoundException("Lending with id " + id + " not found");
        }

        Log.debugf("Lending with id %s found", id);
        return lending;
    }

    @Transactional
    public LendingModel updateLendingStatus(UUID id,
                                            LendingStatus status) {
        LendingEntity lendingEntity = getLendingById(id);

        if (lendingEntity.getStatus() == status) {
            return LendingMapper.INSTANCE.map(lendingEntity); // No change, so return early
        }

        LendingHistoryEntity historyEntity = new LendingHistoryEntity();
        historyEntity.setLendingRequestId(lendingEntity.getId());
        historyEntity.setStatus(lendingEntity.getStatus());     // Storing old status
        historyEntity.setChangedAt(Instant.now());
        historyEntity.persistAndFlush();

        lendingEntity.setStatus(status);
        lendingEntity.setUpdatedAt(Instant.now());
        LendingEntity.flush();

        eventService.sendLendingEvent(lendingEntity);

        return LendingMapper.INSTANCE.map(lendingEntity);
    }

    public List<LendingEntity> getLendingsByReaderId(UUID readerId) {
        return LendingEntity.find("readerId = ?1 order by updatedAt desc", readerId).list();
    }

    public List<LendingEntity> getLendingsByOwnerId(UUID ownerId) {
        return LendingEntity.find("ownerId = ?1 order by updatedAt desc", ownerId).list();
    }

    public List<LendingEntity> getLendingsByReaderIdAndStatus(UUID readerId,
                                                              LendingStatus status) {
        return LendingEntity.find("readerId = ?1 and status = ?2 order by updatedAt desc", readerId, status).list();
    }

    @Transactional
    public List<LendingHistoryEntity> getLendingHistoryByLendingId(UUID lendingId) {
        return LendingHistoryEntity.find("lendingRequestId = ?1 order by changedAt desc", lendingId).list();
    }
}
