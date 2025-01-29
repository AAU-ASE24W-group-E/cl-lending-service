package at.aau.ase.cl.model;

import at.aau.ase.cl.api.interceptor.exceptions.IllegalStatusException;
import at.aau.ase.cl.api.model.LendingStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LendingEntityUnitTest {

    private LendingEntity entity;

    @BeforeEach
    void setUp() {
        entity = new LendingEntity();
    }

    @Nested
    @DisplayName("Valid Transitions")
    class ValidTransitionsTest {

        @ParameterizedTest(name = "From {0} to {1}, expect final status = {2}")
        @MethodSource("at.aau.ase.cl.model.LendingEntityUnitTest#validTransitionsProvider")
        void testValidTransitions(LendingStatus currentStatus,
                                  LendingStatus nextStatus,
                                  LendingStatus expectedFinalStatus) {
            if (currentStatus != null) {
                entity.setStatus(currentStatus);
                assertEquals(currentStatus, entity.getStatus(),
                        "Entity should have been set to the current status first.");
            }

            entity.setStatus(nextStatus);

            assertEquals(expectedFinalStatus, entity.getStatus(),
                    String.format("Transition from %s -> %s should end in %s",
                            currentStatus, nextStatus, expectedFinalStatus));
        }
    }

    @Nested
    @DisplayName("Invalid Transitions")
    class InvalidTransitionsTest {

        @ParameterizedTest(name = "From {0} to {1} should throw IllegalStatusException")
        @MethodSource("at.aau.ase.cl.model.LendingEntityUnitTest#invalidTransitionsProvider")
        void testInvalidTransitions(LendingStatus currentStatus, LendingStatus nextStatus) {
            if (currentStatus != null) {
                entity.setStatus(currentStatus);
                Assertions.assertEquals(currentStatus, entity.getStatus());
            }

            Executable action = () -> entity.setStatus(nextStatus);
            assertThrows(IllegalStatusException.class, action,
                    String.format("Transition from %s to %s should be invalid",
                            currentStatus, nextStatus));
        }
    }

    static Stream<Arguments> validTransitionsProvider() {
        return Stream.of(
                Arguments.of(null, LendingStatus.READER_CREATED_REQUEST, LendingStatus.READER_CREATED_REQUEST),
                Arguments.of(null, LendingStatus.OWNER_SUGGESTED_MEETING, LendingStatus.OWNER_SUGGESTED_MEETING),
                Arguments.of(null, LendingStatus.OWNER_DENIED, LendingStatus.OWNER_DENIED),
                Arguments.of(null, LendingStatus.READER_WITHDREW, LendingStatus.READER_WITHDREW),
                Arguments.of(null, LendingStatus.READER_ACCEPTED_MEETING, LendingStatus.READER_ACCEPTED_MEETING),
                Arguments.of(null, LendingStatus.READER_CONFIRMED_TRANSFER, LendingStatus.READER_CONFIRMED_TRANSFER),
                Arguments.of(null, LendingStatus.OWNER_CONFIRMED_TRANSFER, LendingStatus.OWNER_CONFIRMED_TRANSFER),
                Arguments.of(null, LendingStatus.BORROWED, LendingStatus.BORROWED),
                Arguments.of(null, LendingStatus.READER_RETURNED_BOOK, LendingStatus.READER_RETURNED_BOOK),
                Arguments.of(null, LendingStatus.OWNER_CONFIRMED_RETURNAL, LendingStatus.OWNER_CONFIRMED_RETURNAL),
                Arguments.of(null, LendingStatus.LENDING_COMPLETED, LendingStatus.LENDING_COMPLETED),

                Arguments.of(LendingStatus.READER_CREATED_REQUEST, LendingStatus.OWNER_SUGGESTED_MEETING, LendingStatus.OWNER_SUGGESTED_MEETING),
                Arguments.of(LendingStatus.READER_CREATED_REQUEST, LendingStatus.OWNER_DENIED, LendingStatus.OWNER_DENIED),
                Arguments.of(LendingStatus.READER_CREATED_REQUEST, LendingStatus.READER_WITHDREW, LendingStatus.READER_WITHDREW),

                Arguments.of(LendingStatus.OWNER_SUGGESTED_MEETING, LendingStatus.READER_ACCEPTED_MEETING, LendingStatus.READER_ACCEPTED_MEETING),
                Arguments.of(LendingStatus.OWNER_SUGGESTED_MEETING, LendingStatus.OWNER_DENIED, LendingStatus.OWNER_DENIED),
                Arguments.of(LendingStatus.OWNER_SUGGESTED_MEETING, LendingStatus.READER_WITHDREW, LendingStatus.READER_WITHDREW),

                Arguments.of(LendingStatus.READER_ACCEPTED_MEETING, LendingStatus.READER_CONFIRMED_TRANSFER, LendingStatus.READER_CONFIRMED_TRANSFER),
                Arguments.of(LendingStatus.READER_ACCEPTED_MEETING, LendingStatus.OWNER_CONFIRMED_TRANSFER, LendingStatus.OWNER_CONFIRMED_TRANSFER),
                Arguments.of(LendingStatus.READER_ACCEPTED_MEETING, LendingStatus.READER_WITHDREW, LendingStatus.READER_WITHDREW),
                Arguments.of(LendingStatus.READER_ACCEPTED_MEETING, LendingStatus.OWNER_DENIED, LendingStatus.OWNER_DENIED),

                Arguments.of(LendingStatus.READER_CONFIRMED_TRANSFER, LendingStatus.OWNER_CONFIRMED_TRANSFER, LendingStatus.BORROWED),
                Arguments.of(LendingStatus.READER_CONFIRMED_TRANSFER, LendingStatus.READER_CONFIRMED_TRANSFER, LendingStatus.BORROWED),

                Arguments.of(LendingStatus.OWNER_CONFIRMED_TRANSFER, LendingStatus.READER_CONFIRMED_TRANSFER, LendingStatus.BORROWED),
                Arguments.of(LendingStatus.OWNER_CONFIRMED_TRANSFER, LendingStatus.OWNER_CONFIRMED_TRANSFER, LendingStatus.BORROWED),

                Arguments.of(LendingStatus.BORROWED, LendingStatus.READER_RETURNED_BOOK, LendingStatus.READER_RETURNED_BOOK),
                Arguments.of(LendingStatus.BORROWED, LendingStatus.OWNER_CONFIRMED_RETURNAL, LendingStatus.OWNER_CONFIRMED_RETURNAL),

                Arguments.of(LendingStatus.READER_RETURNED_BOOK, LendingStatus.READER_RETURNED_BOOK, LendingStatus.OWNER_CONFIRMED_RETURNAL),
                Arguments.of(LendingStatus.READER_RETURNED_BOOK, LendingStatus.OWNER_CONFIRMED_RETURNAL, LendingStatus.OWNER_CONFIRMED_RETURNAL),
                Arguments.of(LendingStatus.READER_RETURNED_BOOK, LendingStatus.LENDING_COMPLETED, LendingStatus.LENDING_COMPLETED),

                Arguments.of(LendingStatus.OWNER_CONFIRMED_RETURNAL, LendingStatus.READER_RETURNED_BOOK, LendingStatus.OWNER_CONFIRMED_RETURNAL),
                Arguments.of(LendingStatus.OWNER_CONFIRMED_RETURNAL, LendingStatus.OWNER_CONFIRMED_RETURNAL, LendingStatus.OWNER_CONFIRMED_RETURNAL),
                Arguments.of(LendingStatus.OWNER_CONFIRMED_RETURNAL, LendingStatus.LENDING_COMPLETED, LendingStatus.LENDING_COMPLETED)
        );
    }

    static Stream<Arguments> invalidTransitionsProvider() {
        return Stream.of(
                Arguments.of(LendingStatus.READER_CREATED_REQUEST, LendingStatus.READER_ACCEPTED_MEETING),
                Arguments.of(LendingStatus.READER_CREATED_REQUEST, LendingStatus.READER_CONFIRMED_TRANSFER),
                Arguments.of(LendingStatus.READER_CREATED_REQUEST, LendingStatus.OWNER_CONFIRMED_TRANSFER),
                Arguments.of(LendingStatus.READER_CREATED_REQUEST, LendingStatus.BORROWED),
                Arguments.of(LendingStatus.READER_CREATED_REQUEST, LendingStatus.READER_RETURNED_BOOK),
                Arguments.of(LendingStatus.READER_CREATED_REQUEST, LendingStatus.OWNER_CONFIRMED_RETURNAL),
                Arguments.of(LendingStatus.READER_CREATED_REQUEST, LendingStatus.LENDING_COMPLETED),

                Arguments.of(LendingStatus.OWNER_SUGGESTED_MEETING, LendingStatus.READER_CONFIRMED_TRANSFER),
                Arguments.of(LendingStatus.OWNER_SUGGESTED_MEETING, LendingStatus.OWNER_CONFIRMED_TRANSFER),
                Arguments.of(LendingStatus.OWNER_SUGGESTED_MEETING, LendingStatus.BORROWED),
                Arguments.of(LendingStatus.OWNER_SUGGESTED_MEETING, LendingStatus.READER_RETURNED_BOOK),
                Arguments.of(LendingStatus.OWNER_SUGGESTED_MEETING, LendingStatus.OWNER_CONFIRMED_RETURNAL),
                Arguments.of(LendingStatus.OWNER_SUGGESTED_MEETING, LendingStatus.LENDING_COMPLETED),

                Arguments.of(LendingStatus.READER_ACCEPTED_MEETING, LendingStatus.BORROWED),
                Arguments.of(LendingStatus.READER_ACCEPTED_MEETING, LendingStatus.READER_RETURNED_BOOK),
                Arguments.of(LendingStatus.READER_ACCEPTED_MEETING, LendingStatus.OWNER_CONFIRMED_RETURNAL),
                Arguments.of(LendingStatus.READER_ACCEPTED_MEETING, LendingStatus.LENDING_COMPLETED),

                Arguments.of(LendingStatus.READER_CONFIRMED_TRANSFER, LendingStatus.BORROWED),
                Arguments.of(LendingStatus.READER_CONFIRMED_TRANSFER, LendingStatus.READER_RETURNED_BOOK),
                Arguments.of(LendingStatus.READER_CONFIRMED_TRANSFER, LendingStatus.OWNER_CONFIRMED_RETURNAL),
                Arguments.of(LendingStatus.READER_CONFIRMED_TRANSFER, LendingStatus.LENDING_COMPLETED),

                Arguments.of(LendingStatus.OWNER_CONFIRMED_TRANSFER, LendingStatus.BORROWED),
                Arguments.of(LendingStatus.OWNER_CONFIRMED_TRANSFER, LendingStatus.READER_RETURNED_BOOK),
                Arguments.of(LendingStatus.OWNER_CONFIRMED_TRANSFER, LendingStatus.OWNER_CONFIRMED_RETURNAL),
                Arguments.of(LendingStatus.OWNER_CONFIRMED_TRANSFER, LendingStatus.LENDING_COMPLETED),

                Arguments.of(LendingStatus.BORROWED, LendingStatus.READER_ACCEPTED_MEETING),
                Arguments.of(LendingStatus.BORROWED, LendingStatus.OWNER_DENIED),
                Arguments.of(LendingStatus.BORROWED, LendingStatus.LENDING_COMPLETED),

                Arguments.of(LendingStatus.READER_RETURNED_BOOK, LendingStatus.READER_ACCEPTED_MEETING),
                Arguments.of(LendingStatus.READER_RETURNED_BOOK, LendingStatus.OWNER_DENIED),
                Arguments.of(LendingStatus.READER_RETURNED_BOOK, LendingStatus.BORROWED),

                Arguments.of(LendingStatus.OWNER_CONFIRMED_RETURNAL, LendingStatus.OWNER_SUGGESTED_MEETING),
                Arguments.of(LendingStatus.OWNER_CONFIRMED_RETURNAL, LendingStatus.READER_CREATED_REQUEST),
                Arguments.of(LendingStatus.OWNER_CONFIRMED_RETURNAL, LendingStatus.BORROWED),

                Arguments.of(LendingStatus.READER_WITHDREW, LendingStatus.READER_ACCEPTED_MEETING),
                Arguments.of(LendingStatus.READER_WITHDREW, LendingStatus.LENDING_COMPLETED),
                Arguments.of(LendingStatus.OWNER_DENIED, LendingStatus.OWNER_SUGGESTED_MEETING),
                Arguments.of(LendingStatus.OWNER_DENIED, LendingStatus.READER_CONFIRMED_TRANSFER),
                Arguments.of(LendingStatus.LENDING_COMPLETED, LendingStatus.BORROWED),
                Arguments.of(LendingStatus.LENDING_COMPLETED, LendingStatus.READER_CREATED_REQUEST),
                Arguments.of(LendingStatus.LENDING_COMPLETED, LendingStatus.READER_RETURNED_BOOK)
        );
    }
}
