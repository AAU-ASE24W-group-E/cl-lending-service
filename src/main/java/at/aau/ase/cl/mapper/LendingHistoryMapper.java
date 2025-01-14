package at.aau.ase.cl.mapper;

import at.aau.ase.cl.api.model.LendingHistoryModel;
import at.aau.ase.cl.model.LendingHistoryEntity;
import org.mapstruct.factory.Mappers;

public interface LendingHistoryMapper {
    LendingHistoryMapper INSTANCE = Mappers.getMapper(LendingHistoryMapper.class);

    LendingHistoryEntity map(LendingHistoryModel model);

    LendingHistoryModel map(LendingHistoryEntity entity);
}
