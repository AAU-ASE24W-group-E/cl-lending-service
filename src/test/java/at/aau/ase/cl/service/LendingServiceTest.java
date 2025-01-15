package at.aau.ase.cl.service;

import at.aau.ase.cl.api.interceptor.exceptions.NotFoundException;
import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.api.model.LendingStatus;
import at.aau.ase.cl.model.LendingEntity;
import at.aau.ase.cl.model.LendingHistoryEntity;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;

import java.util.List;
import java.util.UUID;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
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
    void testGetLendingByIdNotFound() {
        UUID randomId = UUID.randomUUID();
        Executable executable = () -> lendingService.getLendingById(randomId);

        NotFoundException ex = assertThrows(NotFoundException.class, executable);
        assertTrue(ex.getMessage().contains(randomId.toString()));
    }

    @Test
    void testUpdateLendingStatusNoChange() {
        LendingModel lending = new LendingModel();
        lending.setBookId(testBookId);
        lending.setOwnerId(testOwnerId);
        lending.setReaderId(testReaderId);
        lending.setStatus(LendingStatus.BORROWED);

        LendingModel createdLending = lendingService.createLending(lending);
        UUID lendingId = createdLending.getId();

        LendingModel updatedLending = lendingService.updateLendingStatus(lendingId, LendingStatus.BORROWED);
        assertEquals(LendingStatus.BORROWED, updatedLending.getStatus());

        List<LendingHistoryEntity> historyList = lendingService.getLendingHistoryByLendingId(lendingId); // History entries should remain the same count
        assertEquals(0, historyList.size(), "No additional history entry should have been created");
    }

    @Test
    void testUpdateLendingStatusChange() {
        LendingModel lending = new LendingModel();
        lending.setBookId(testBookId);
        lending.setOwnerId(testOwnerId);
        lending.setReaderId(testReaderId);
        lending.setStatus(LendingStatus.BORROWED);

        LendingModel createdLending = lendingService.createLending(lending);
        UUID lendingId = createdLending.getId();

        LendingModel updatedLending = lendingService.updateLendingStatus(lendingId, LendingStatus.OWNER_DENIED);
        assertEquals(LendingStatus.OWNER_DENIED, updatedLending.getStatus());

        List<LendingHistoryEntity> historyList = lendingService.getLendingHistoryByLendingId(lendingId);
        assertFalse(historyList.isEmpty());

        LendingHistoryEntity historyEntity = historyList.get(0);
        assertEquals(LendingStatus.BORROWED, historyEntity.getStatus());
    }

    @Test
    void testGetLendingsByReaderId() {
        LendingModel lending = new LendingModel();
        lending.setBookId(testBookId);
        lending.setOwnerId(testOwnerId);
        lending.setReaderId(testReaderId);
        lending.setStatus(LendingStatus.BORROWED);

        LendingModel createdLending = lendingService.createLending(lending);
        UUID lendingId = createdLending.getId();

        List<LendingEntity> lendings = lendingService.getLendingsByReaderId(testReaderId);

        assertEquals(lendingId, lendings.get(0).getId());
    }

    @Test
    void testGetLendingsByReaderIdError() {
        UUID randomId = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> lendingService.getLendingsByReaderId(randomId));
    }

    @Test
    void testGetLendingsByOwnerId() {
        LendingModel lending = new LendingModel();
        lending.setStatus(LendingStatus.BORROWED);
        lending.setBookId(testBookId);
        lending.setOwnerId(testOwnerId);
        lending.setReaderId(testReaderId);

        LendingModel createdLending = lendingService.createLending(lending);
        UUID lendingId = createdLending.getId();

        List<LendingEntity> lendings = lendingService.getLendingsByOwnerId(testOwnerId);
        assertEquals(lendingId, lendings.get(0).getId());
    }

    @Test
    void testGetLendingsByOwnerIdError() {
        UUID randomId = UUID.randomUUID();
        assertThrows(NotFoundException.class, () -> lendingService.getLendingsByOwnerId(randomId));
    }

    @Test
    void testGetLendingsByReaderAndStatus() {
        LendingModel lending = new LendingModel();
        lending.setReaderId(testReaderId);
        lending.setBookId(testBookId);
        lending.setOwnerId(testOwnerId);
        lending.setStatus(LendingStatus.BORROWED);

        LendingModel createdLending = lendingService.createLending(lending);
        UUID lendingId = createdLending.getId();

        List<LendingEntity> lendings = lendingService.getLendingsByReaderIdAndStatus(testReaderId, LendingStatus.BORROWED);
        assertEquals(lendingId, lendings.get(0).getId());
    }
}