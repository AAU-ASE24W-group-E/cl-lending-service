package at.aau.ase.cl.mapper;

import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.model.LendingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LendingMapper {
    LendingMapper INSTANCE = Mappers.getMapper(LendingMapper.class);

    LendingEntity map(LendingModel model);

    LendingModel map(LendingEntity entity);
}
