package at.aau.ase.cl.mapper;

import at.aau.ase.cl.api.model.Lending;
import at.aau.ase.cl.model.LendingEntity;
import org.mapstruct.factory.Mappers;

public interface LendingMapper {
    LendingMapper INSTANCE = Mappers.getMapper(LendingMapper.class);

    LendingEntity map(Lending model);

    Lending map(LendingEntity entity);
}
