package at.aau.ase.cl.mapper;

import at.aau.ase.cl.api.model.LendingHistory;
import at.aau.ase.cl.model.LendingHistoryEntity;
import org.mapstruct.factory.Mappers;

public interface LendingHistoryMapper {
    LendingHistoryMapper INSTANCE = Mappers.getMapper(LendingHistoryMapper.class);

    LendingHistoryEntity map(LendingHistory model);

    LendingHistory map(LendingHistoryEntity entity);
}
