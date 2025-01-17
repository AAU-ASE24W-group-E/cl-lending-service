package at.aau.ase.cl.mapper;

import at.aau.ase.cl.api.model.LendingMeetingModel;
import at.aau.ase.cl.model.LendingMeetingEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface LendingMeetingMapper {
    LendingMeetingMapper INSTANCE = Mappers.getMapper(LendingMeetingMapper.class);

    LendingMeetingEntity map(LendingMeetingModel model);

    LendingMeetingModel map(LendingMeetingEntity entity);
}
