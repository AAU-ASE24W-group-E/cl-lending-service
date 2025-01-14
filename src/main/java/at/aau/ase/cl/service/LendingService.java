package at.aau.ase.cl.service;

import at.aau.ase.cl.api.interceptor.NotFoundException;
import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.api.model.LendingStatus;
import at.aau.ase.cl.mapper.LendingMapper;
import at.aau.ase.cl.model.LendingEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class LendingService {
    private final Logger logger;

    @jakarta.inject.Inject
    public LendingService(Logger logger) {
        this.logger = logger;
    }

    @Transactional
    public LendingModel createLending(LendingModel lendingModel) {
        LendingEntity lendingEntity = LendingMapper.INSTANCE.map(lendingModel);

        lendingEntity.persistAndFlush();

        return LendingMapper.INSTANCE.map(lendingEntity);
    }

    public LendingEntity getLendingById(UUID id) {
        LendingEntity lending = LendingEntity.findById(id);

        if (lending == null) {
            logger.debugf("Lending with id %s not found", id);
            throw new NotFoundException("Lending with id " + id + " not found");
        }

        logger.debugf("Lending with id %s found", id);
        return lending;
    }

    public List<LendingEntity> getLendingsByReaderId(UUID readerId) {
        List<LendingEntity> lendings = LendingEntity.find("ownerId = ?1 order by updatedAt desc", readerId).list();
        if (lendings.isEmpty()) {
            throw new NotFoundException("No lendings found for reader with ID: " + readerId);
        }
        return lendings;
    }

    public List<LendingEntity> getLendingsByOwnerId(UUID ownerId) {
        List<LendingEntity> lendings = LendingEntity.find("ownerId = ?1 order by updatedAt desc", ownerId).list();
        if (lendings.isEmpty()) {
            throw new NotFoundException("No lendings found for owner with ID: " + ownerId);
        }
        return lendings;
    }

    public List<LendingEntity> getLendingsByReaderIdAndStatus(UUID readerId, LendingStatus status) {
        return LendingEntity.find("readerId = ?1 and status = ?2 order by updatedAt desc", readerId, status).list();
    }
}
