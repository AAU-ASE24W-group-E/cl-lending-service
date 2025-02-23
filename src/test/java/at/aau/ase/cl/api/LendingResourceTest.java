package at.aau.ase.cl.api;

import at.aau.ase.cl.api.model.LendingMeetingModel;
import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.api.model.LendingStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class LendingResourceTest {
    private UUID bookId;
    private UUID readerId;
    private UUID ownerId;

    @BeforeEach
    void setUp() {
        bookId = UUID.randomUUID();
        readerId = UUID.randomUUID();
        ownerId = UUID.randomUUID();
    }

    @Test
    void testCreateLending() {
        LendingModel lending = new LendingModel(bookId, readerId, ownerId, null, LendingStatus.BORROWED);

        given()
                .contentType(ContentType.JSON)
                .body(lending)
                .post("/lendings")
                .then()
                .statusCode(200)
                .log().body(true)
                .body("bookId", equalTo(bookId.toString()))
                .body("readerId", equalTo(readerId.toString()))
                .body("ownerId", equalTo(ownerId.toString()))
                .body("status", equalTo(LendingStatus.BORROWED.toString()));
    }

    @Test
    void testCreateLendingError() {
        LendingModel lending = new LendingModel(bookId, ownerId, ownerId, null, LendingStatus.BORROWED);

        given()
                .contentType(ContentType.JSON)
                .body(lending)
                .post("/lendings")
                .then()
                .statusCode(409)
                .log().body(true)
                .body("type", equalTo("InvalidOwnerReaderException"))
                .body("message", equalTo("Owner cannot create lending request for their own book"));
    }


    @Test
    void testGetLending() {
        String id = createLendingAndGetId(LendingStatus.BORROWED);

        given()
                .contentType(ContentType.JSON)
                .get("/lendings/" + id)
                .then()
                .statusCode(200)
                .log().body(true)
                .body("id", equalTo(id))
                .body("bookId", equalTo(bookId.toString()))
                .body("readerId", equalTo(readerId.toString()))
                .body("ownerId", equalTo(ownerId.toString()))
                .body("status", equalTo(LendingStatus.BORROWED.toString()));
    }

    @Test
    void testGetLendingsByReaderId() {
        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();
        String lendingId1 = createLendingWithSpecificBook(LendingStatus.READER_CREATED_REQUEST, bookId1);
        String lendingId2 = createLendingWithSpecificBook(LendingStatus.READER_CREATED_REQUEST, bookId2);

        given()
                .contentType(ContentType.JSON)
                .get("/lendings/readers/" + readerId)
                .then()
                .statusCode(200)
                .log().body(true)
                .body("$.size()", equalTo(2))
                .body("[0].id", is(lendingId2))     // Order by updatedAt desc
                .body("[1].id", is(lendingId1))
                .body("[0].readerId", equalTo(readerId.toString()))
                .body("[1].readerId", equalTo(readerId.toString()))
                .body("[0].ownerId", equalTo(ownerId.toString()))
                .body("[1].ownerId", equalTo(ownerId.toString()))
                .body("[0].bookId", equalTo(bookId2.toString()))
                .body("[1].bookId", equalTo(bookId1.toString()))
                .body("[0].status", equalTo(LendingStatus.READER_CREATED_REQUEST.toString()))
                .body("[1].status", equalTo(LendingStatus.READER_CREATED_REQUEST.toString()));
    }

    @Test
    void testGetLendingsByReaderIdOfSpecificStatus() {
        UUID bookId1 = UUID.randomUUID();
        String lendingId1 = createLendingWithSpecificBook(LendingStatus.READER_CREATED_REQUEST, bookId1);

        createLendingAndGetId(LendingStatus.OWNER_DENIED);

        given()
                .contentType(ContentType.JSON)
                .get("/lendings/readers/" + readerId + "?status=READER_CREATED_REQUEST")
                .then()
                .statusCode(200)
                .log().body(true)
                .body("$.size()", equalTo(1))
                .body("[0].id", is(lendingId1))
                .body("[0].readerId", equalTo(readerId.toString()))
                .body("[0].ownerId", equalTo(ownerId.toString()))
                .body("[0].bookId", equalTo(bookId1.toString()))
                .body("[0].status", equalTo(LendingStatus.READER_CREATED_REQUEST.toString()));
    }

    @Test
    void testGetLendingsByOwnerId() {
        UUID bookId1 = UUID.randomUUID();
        UUID bookId2 = UUID.randomUUID();

        String lendingId1 = createLendingWithSpecificBook(LendingStatus.BORROWED, bookId1);
        String lendingId2 = createLendingWithSpecificBook(LendingStatus.BORROWED, bookId2);

        given()
                .contentType(ContentType.JSON)
                .get("/lendings/owners/" + ownerId)
                .then()
                .statusCode(200)
                .log().body(true)
                .body("$.size()", equalTo(2))
                .body("[0].id", is(lendingId2))     // Order by updatedAt desc
                .body("[1].id", is(lendingId1))
                .body("[0].readerId", equalTo(readerId.toString()))
                .body("[1].readerId", equalTo(readerId.toString()))
                .body("[0].ownerId", equalTo(ownerId.toString()))
                .body("[1].ownerId", equalTo(ownerId.toString()))
                .body("[0].bookId", equalTo(bookId2.toString()))
                .body("[1].bookId", equalTo(bookId1.toString()))
                .body("[0].status", equalTo(LendingStatus.BORROWED.toString()))
                .body("[1].status", equalTo(LendingStatus.BORROWED.toString()));
    }

    @Test
    void testUpdateLendingStatus() {
        String id = createLendingAndGetId(LendingStatus.READER_CREATED_REQUEST);

        given()
                .contentType(ContentType.JSON)
                .patch("/lendings/" + id + "?status=OWNER_DENIED")
                .then()
                .statusCode(200)
                .log().body(true)
                .body("id", equalTo(id))
                .body("bookId", equalTo(bookId.toString()))
                .body("readerId", equalTo(readerId.toString()))
                .body("ownerId", equalTo(ownerId.toString()))
                .body("status", equalTo(LendingStatus.OWNER_DENIED.toString()));
    }

    @Test
    void testUpdateLendingStatusBlank() {
        String id = createLendingAndGetId(LendingStatus.BORROWED);

        given()
                .contentType(ContentType.JSON)
                .patch("/lendings/" + id + "?status")
                .then()
                .statusCode(404)
                .log().body(true)
                .body("type", equalTo("IllegalStatusException"))
                .body("message", equalTo("Status parameter is required."));

        given()
                .contentType(ContentType.JSON)
                .patch("/lendings/" + id + "?status=" + " ")
                .then()
                .statusCode(404)
                .log().body(true)
                .body("type", equalTo("IllegalStatusException"))
                .body("message", equalTo("Status parameter is required."));
    }

    @Test
    void testUpdateLendingWrongStatus() {
        String id = createLendingAndGetId(LendingStatus.BORROWED);

        given()
                .contentType(ContentType.JSON)
                .patch("/lendings/" + id + "?status=OTHER_STATUS")
                .then()
                .statusCode(404)
                .log().body(true)
                .body("type", equalTo("IllegalStatusException"))
                .body("message", equalTo("Invalid status: OTHER_STATUS. Valid statuses are: " + Arrays.toString(LendingStatus.values())));
    }


    @Test
    void testGetLendingHistory() {
        String id = createLendingAndGetId(LendingStatus.READER_CREATED_REQUEST);

        given()
                .contentType(ContentType.JSON)
                .patch("/lendings/" + id + "?status=OWNER_DENIED")
                .then()
                .statusCode(200)
                .log().body(true)
                .body("id", equalTo(id))
                .body("bookId", equalTo(bookId.toString()))
                .body("readerId", equalTo(readerId.toString()))
                .body("ownerId", equalTo(ownerId.toString()))
                .body("status", equalTo(LendingStatus.OWNER_DENIED.toString()))
                .extract();

        given()
                .contentType(ContentType.JSON)
                .get("/lendings/" + id + "/history")
                .then()
                .statusCode(200)
                .log().body(true)
                .body("$.size()", equalTo(1))
                .body("[0].lendingRequestId", equalTo(id))
                .body("[0].status", equalTo(LendingStatus.READER_CREATED_REQUEST.toString()))
                .extract();
    }


    private String createLendingAndGetId(LendingStatus status) {
        LendingModel lending = new LendingModel(bookId, readerId, ownerId, null, status);

        return given()
                .contentType(ContentType.JSON)
                .body(lending)
                .post("/lendings")
                .then()
                .statusCode(200)
                .log().body(true)
                .body("bookId", equalTo(bookId.toString()))
                .body("ownerId", equalTo(ownerId.toString()))
                .body("readerId", equalTo(readerId.toString()))
                .body("status", equalTo(status.toString()))
                .extract()
                .path("id");
    }

    private String createLendingWithSpecificBook(LendingStatus status,
                                                 UUID book) {
        LendingModel lending = new LendingModel(book, readerId, ownerId, null, status);

        return given()
                .contentType(ContentType.JSON)
                .body(lending)
                .post("/lendings")
                .then()
                .statusCode(200)
                .log().body(true)
                .body("bookId", equalTo(book.toString()))
                .body("ownerId", equalTo(ownerId.toString()))
                .body("readerId", equalTo(readerId.toString()))
                .body("status", equalTo(status.toString()))
                .extract()
                .path("id");
    }


    @Test
    void testPostMeetingSuccessfully() {
        String id = createLendingAndGetId(LendingStatus.READER_CREATED_REQUEST);
        LendingMeetingModel meeting = new LendingMeetingModel(Instant.now().plus(30, ChronoUnit.DAYS),
                "Location", Instant.now().plus(30, ChronoUnit.DAYS));

        given()
                .contentType(ContentType.JSON)
                .body(meeting)
                .post("/lendings/" + id + "/meeting")
                .then()
                .statusCode(200)
                .log().body(true)
                .body("bookId", equalTo(bookId.toString()))
                .body("readerId", equalTo(readerId.toString()))
                .body("ownerId", equalTo(ownerId.toString()))
                .body("status", equalTo(LendingStatus.OWNER_SUGGESTED_MEETING.toString()))
                .body("lendingMeeting.meetingLocation", equalTo("Location"));

    }

    @Test
    void testPostEmptyMeetingLocation() {
        String id = createLendingAndGetId(LendingStatus.BORROWED);
        LendingMeetingModel meeting = new LendingMeetingModel(Instant.now().plus(30, ChronoUnit.DAYS),
                "", Instant.now().plus(30, ChronoUnit.DAYS));

        given()
                .contentType(ContentType.JSON)
                .body(meeting)
                .post("/lendings/" + id + "/meeting")
                .then()
                .statusCode(400)
                .log().body(true)
                .body("type", equalTo("IllegalMeetingException"))
                .body("message", equalTo("Meeting place parameter is required."));
    }

    @Test
    void testPostEmptyDeadline() {
        String id = createLendingAndGetId(LendingStatus.BORROWED);
        LendingMeetingModel meeting = new LendingMeetingModel(Instant.now().plus(30, ChronoUnit.DAYS),
                "Location", null);

        given()
                .contentType(ContentType.JSON)
                .body(meeting)
                .post("/lendings/" + id + "/meeting")
                .then()
                .statusCode(400)
                .log().body(true)
                .body("type", equalTo("IllegalMeetingException"))
                .body("message", equalTo("Deadline parameter is required."));
    }

    @Test
    void testPostEmptyMeetingTime() {
        String id = createLendingAndGetId(LendingStatus.BORROWED);
        LendingMeetingModel meeting = new LendingMeetingModel(null,
                "Location", Instant.now().plus(30, ChronoUnit.DAYS));

        given()
                .contentType(ContentType.JSON)
                .body(meeting)
                .post("/lendings/" + id + "/meeting")
                .then()
                .statusCode(400)
                .log().body(true)
                .body("type", equalTo("IllegalMeetingException"))
                .body("message", equalTo("Meeting time parameter is required."));
    }

    @Test
    void testPostPastMeetingTime() {
        String id = createLendingAndGetId(LendingStatus.BORROWED);
        LendingMeetingModel meeting = new LendingMeetingModel(Instant.now().minus(30, ChronoUnit.DAYS),
                "Location", Instant.now().plus(30, ChronoUnit.DAYS));

        given()
                .contentType(ContentType.JSON)
                .body(meeting)
                .post("/lendings/" + id + "/meeting")
                .then()
                .statusCode(400)
                .log().body(true)
                .body("type", equalTo("IllegalMeetingException"))
                .body("message", equalTo("Dates have to be in the future."));
    }

    @Test
    void testPostPastDeadline() {
        String id = createLendingAndGetId(LendingStatus.BORROWED);
        LendingMeetingModel meeting = new LendingMeetingModel(Instant.now().minus(30, ChronoUnit.DAYS),
                "Location", Instant.now().minus(30, ChronoUnit.DAYS));

        given()
                .contentType(ContentType.JSON)
                .body(meeting)
                .post("/lendings/" + id + "/meeting")
                .then()
                .statusCode(400)
                .log().body(true)
                .body("type", equalTo("IllegalMeetingException"))
                .body("message", equalTo("Dates have to be in the future."));
    }
}

