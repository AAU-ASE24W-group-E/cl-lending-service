package at.aau.ase.cl.service;

import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.api.model.LendingStatus;
import at.aau.ase.cl.mapper.LendingMapper;
import at.aau.ase.cl.model.LendingEntity;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
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
        return LendingEntity.find("readerId = ?1 order by updatedAt desc", readerId).list();
    }

    public List<LendingEntity> getLendingsByOwnerId(UUID ownerId) {
        return LendingEntity.find("ownerId = ?1 order by updatedAt desc", ownerId).list();
    }

    public List<LendingEntity> getLendingsByReaderIdAndStatus(UUID readerId, LendingStatus status) {
        return LendingEntity.find("readerId = ?1 and status = ?2 order by updatedAt desc", readerId, status).list();
    }
}
