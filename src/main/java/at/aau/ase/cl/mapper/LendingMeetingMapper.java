package at.aau.ase.cl.mapper;

import at.aau.ase.cl.api.model.LendingMeeting;
import at.aau.ase.cl.model.LendingMeetingEntity;
import org.mapstruct.factory.Mappers;

public interface LendingMeetingMapper {
    LendingMeetingMapper INSTANCE = Mappers.getMapper(LendingMeetingMapper.class);

    LendingMeetingEntity map(LendingMeeting model);

    LendingMeeting map(LendingMeetingEntity entity);
}
