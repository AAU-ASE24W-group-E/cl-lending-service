package at.aau.ase.cl.service;

import at.aau.ase.cl.api.interceptor.exceptions.NotFoundException;
import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.api.model.LendingStatus;
import at.aau.ase.cl.model.LendingEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.util.UUID;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LendingServiceTest {
    @Inject
    LendingService lendingService;

    private static UUID testLendingId;
    private static UUID testReaderId;
    private static UUID testOwnerId;
    private static UUID testBookId;

    @BeforeAll
    static void initUuids() {
        testReaderId = UUID.randomUUID();
        testOwnerId = UUID.randomUUID();
        testBookId = UUID.randomUUID();
    }

    @Test
    @Order(1)
    void testCreateLending() {
        LendingModel lending = new LendingModel();
        lending.setBookId(testBookId);
        lending.setReaderId(testReaderId);
        lending.setOwnerId(testOwnerId);
        lending.setStatus(LendingStatus.BORROWED);

        LendingModel createdLending = lendingService.createLending(lending);

        assertNotNull(createdLending);
        assertNotNull(createdLending.getId());
        assertEquals(testReaderId, createdLending.getReaderId());
        assertEquals(testOwnerId, createdLending.getOwnerId());
        assertEquals(testBookId, createdLending.getBookId());
        assertEquals(LendingStatus.BORROWED, createdLending.getStatus());

        testLendingId = createdLending.getId();
    }

    @Test
    @Order(2)
    void testGetLendingByIdFound() {
        LendingModel lending = new LendingModel();
        lending.setBookId(testBookId);
        lending.setReaderId(testReaderId);
        lending.setOwnerId(testOwnerId);
        lending.setStatus(LendingStatus.BORROWED);

        LendingModel createdLending = lendingService.createLending(lending);
        UUID lendingId = createdLending.getId();

        LendingEntity lendingEntity = lendingService.getLendingById(lendingId);
        assertNotNull(lendingEntity);
        assertEquals(lendingId, lendingEntity.getId());
        assertEquals(testReaderId, lendingEntity.getReaderId());
        assertEquals(testOwnerId, lendingEntity.getOwnerId());
        assertEquals(testBookId, lendingEntity.getBookId());
        assertEquals(LendingStatus.BORROWED, lendingEntity.getStatus());
    }

    @Test
    @Order(3)
    void testGetLendingByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        Executable executable = () -> lendingService.getLendingById(randomId);

        NotFoundException ex = assertThrows(NotFoundException.class, executable);
        assertTrue(ex.getMessage().contains(randomId.toString()));
    }

}
