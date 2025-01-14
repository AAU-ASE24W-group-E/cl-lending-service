package at.aau.ase.cl.service;

import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.mapper.LendingMapper;
import at.aau.ase.cl.model.LendingEntity;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.jboss.logging.Logger;

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
}
