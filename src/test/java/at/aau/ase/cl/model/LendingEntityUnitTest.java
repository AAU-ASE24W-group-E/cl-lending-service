package at.aau.ase.cl.model;

import at.aau.ase.cl.api.model.LendingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class LendingEntityUnitTest {
    LendingEntity entity;

    @BeforeEach
    void setUp() {
        entity = new LendingEntity();
    }

    @ParameterizedTest
    @MethodSource("generateValidStatusTransitions")
    void validStatusTransitionShouldSetStatus(List<LendingStatus> statusTransition) {
        assertNull(entity.getStatus());

        for (var status : statusTransition) {
            entity.setStatus(status);
        }

        assertNotNull(entity.getStatus());
    }

    static Stream<Arguments> generateValidStatusTransitions() {
        List<List<LendingStatus>> statesTree = new LinkedList<>();
        List<LendingStatus> lastState = List.of(LendingStatus.READER_CREATED_REQUEST);
        statesTree.add(lastState);

        lastState = new LinkedList<>(lastState);
        lastState.add(LendingStatus.OWNER_DENIED);
        statesTree.add(lastState);

        lastState = new LinkedList<>(lastState);
        lastState.removeLast();
        lastState.add(LendingStatus.READER_WITHDREW);
        statesTree.add(lastState);

        lastState = new LinkedList<>(lastState);
        lastState.removeLast();
        lastState.add(LendingStatus.OWNER_SUGGESTED_MEETING);
        statesTree.add(lastState);

        lastState = new LinkedList<>(lastState);
        lastState.add(LendingStatus.OWNER_DENIED);
        statesTree.add(lastState);
        lastState = new LinkedList<>(lastState);
        lastState.removeLast();
        lastState.add(LendingStatus.READER_WITHDREW);
        statesTree.add(lastState);

        lastState = new LinkedList<>(lastState);
        lastState.removeLast();
        lastState.add(LendingStatus.READER_ACCEPTED_MEETING);
        statesTree.add(lastState);
        lastState = new LinkedList<>(lastState);
        lastState.add(LendingStatus.OWNER_DENIED);
        statesTree.add(lastState);
        lastState = new LinkedList<>(lastState);
        lastState.removeLast();
        lastState.add(LendingStatus.READER_WITHDREW);
        statesTree.add(lastState);

        lastState = new LinkedList<>(lastState);
        lastState.removeLast();
        lastState.add(LendingStatus.READER_CONFIRMED_TRANSFER);
        statesTree.add(lastState);
        lastState = new LinkedList<>(lastState);
        lastState.add(LendingStatus.OWNER_CONFIRMED_TRANSFER);
        statesTree.add(lastState);
        // borrowed
        lastState = new LinkedList<>(lastState);
        lastState.add(LendingStatus.READER_RETURNED_BOOK);
        statesTree.add(lastState);
        lastState = new LinkedList<>(lastState);
        lastState.add(LendingStatus.OWNER_CONFIRMED_RETURNAL);
        statesTree.add(lastState);

        return statesTree.stream().map(Arguments::of);
    }

}