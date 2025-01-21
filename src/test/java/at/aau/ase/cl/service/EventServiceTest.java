package at.aau.ase.cl.service;

import at.aau.ase.cl.api.model.LendingMeetingModel;
import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.api.model.LendingStatus;
import at.aau.ase.cl.event.LendingEvent;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.kafka.InjectKafkaCompanion;
import io.quarkus.test.kafka.KafkaCompanionResource;
import io.restassured.http.ContentType;
import io.smallrye.reactive.messaging.kafka.companion.ConsumerTask;
import io.smallrye.reactive.messaging.kafka.companion.KafkaCompanion;
import jakarta.inject.Inject;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serdes;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@QuarkusTestResource(KafkaCompanionResource.class)
class EventServiceTest {

    @InjectKafkaCompanion
    KafkaCompanion kafka;

    Deserializer<UUID> keyDeserializer;
    Deserializer<LendingEvent> valueDeserializer;

    ConsumerTask<UUID, LendingEvent> events;

    @BeforeEach
    void setUp() {
        keyDeserializer = Serdes.serdeFrom(UUID.class).deserializer();
        valueDeserializer = new ObjectMapperDeserializer<>(LendingEvent.class);

        events = kafka.consumeWithDeserializers(keyDeserializer, valueDeserializer)
                .withOffsetReset(OffsetResetStrategy.LATEST)
                .withGroupId(UUID.randomUUID().toString())
                .withAutoCommit()
                .fromTopics("cl.lending");
    }

    @AfterEach
    void tearDown() {
        events.close();
        keyDeserializer.close();
        valueDeserializer.close();
    }

    @Test
    void changeLendingStatusShouldEmitEvent() {
        LendingModel lending = given()
                .contentType(ContentType.JSON)
                .body(new LendingModel(UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID(), null,
                        LendingStatus.READER_CREATED_REQUEST))
                .post("/lendings")
                .then()
                .statusCode(200)
                .extract().as(LendingModel.class);

        given().contentType(ContentType.JSON)
                .body(new LendingMeetingModel(
                        Instant.now().plusSeconds(3600*24),
                        "test location",
                        Instant.now().plusSeconds(3600*24*7)))
                .post("/lendings/{id}/meeting", lending.getId())
                .then()
                .statusCode(200);

        LendingStatus updateStatus = LendingStatus.READER_ACCEPTED_MEETING;
        given()
                .queryParam("status", updateStatus.toString())
                .patch("/lendings/{id}", lending.getId())
                .then()
                .statusCode(200);

        events.awaitRecords(3, Duration.ofSeconds(5));
        var records = events.getRecords();
        var rec1 = records.getFirst();
        assertEquals(lending.getBookId(), rec1.key());
        assertEquals(lending.getBookId(), rec1.value().bookId());
        assertEquals(lending.getOwnerId(), rec1.value().ownerId());
        assertEquals(lending.getStatus(), rec1.value().status());

        var rec2 = records.get(1);
        assertEquals(lending.getBookId(), rec2.key());
        assertEquals(lending.getBookId(), rec2.value().bookId());
        assertEquals(lending.getOwnerId(), rec2.value().ownerId());
        assertEquals(LendingStatus.OWNER_SUGGESTED_MEETING, rec2.value().status());

        var rec3 = records.get(2);
        assertEquals(lending.getBookId(), rec3.key());
        assertEquals(lending.getBookId(), rec3.value().bookId());
        assertEquals(lending.getOwnerId(), rec3.value().ownerId());
        assertEquals(updateStatus, rec3.value().status());
    }
}