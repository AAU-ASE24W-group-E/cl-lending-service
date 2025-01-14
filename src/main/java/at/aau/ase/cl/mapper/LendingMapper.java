package at.aau.ase.cl.mapper;

import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.model.LendingEntity;
import org.mapstruct.factory.Mappers;

public interface LendingMapper {
    LendingMapper INSTANCE = Mappers.getMapper(LendingMapper.class);

    LendingEntity map(LendingModel model);

    LendingModel map(LendingEntity entity);
}
