package at.aau.ase.cl.api;

import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.api.model.LendingStatus;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
        LendingModel lending = new LendingModel(bookId, readerId, ownerId, LendingStatus.BORROWED);

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
        String lendingId1 = createLendingAndGetId(LendingStatus.BORROWED);
        String lendingId2 = createLendingAndGetId(LendingStatus.BORROWED);

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
                .body("[0].bookId", equalTo(bookId.toString()))
                .body("[1].bookId", equalTo(bookId.toString()))
                .body("[0].status", equalTo(LendingStatus.BORROWED.toString()))
                .body("[1].status", equalTo(LendingStatus.BORROWED.toString()));
    }

    @Test
    void testGetLendingsByReaderIdOfSpecificStatus() {
        String lendingId1 = createLendingAndGetId(LendingStatus.BORROWED);
        createLendingAndGetId(LendingStatus.OWNER_DENIED);

        given()
                .contentType(ContentType.JSON)
                .get("/lendings/readers/" + readerId + "?status=BORROWED")
                .then()
                .statusCode(200)
                .log().body(true)
                .body("$.size()", equalTo(1))
                .body("[0].id", is(lendingId1))
                .body("[0].readerId", equalTo(readerId.toString()))
                .body("[0].ownerId", equalTo(ownerId.toString()))
                .body("[0].bookId", equalTo(bookId.toString()))
                .body("[0].status", equalTo(LendingStatus.BORROWED.toString()));
    }

    @Test
    void testGetLendingsByOwnerId() {
        String lendingId1 = createLendingAndGetId(LendingStatus.BORROWED);
        String lendingId2 = createLendingAndGetId(LendingStatus.BORROWED);

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
                .body("[0].bookId", equalTo(bookId.toString()))
                .body("[1].bookId", equalTo(bookId.toString()))
                .body("[0].status", equalTo(LendingStatus.BORROWED.toString()))
                .body("[1].status", equalTo(LendingStatus.BORROWED.toString()));
    }

    @Test
    void testUpdateLendingStatus() {
        String id = createLendingAndGetId(LendingStatus.BORROWED);

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
        String id = createLendingAndGetId(LendingStatus.BORROWED);

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
                .body("[0].status", equalTo(LendingStatus.BORROWED.toString()))
                .extract();
    }


    private String createLendingAndGetId(LendingStatus status) {
        LendingModel lending = new LendingModel(bookId, readerId, ownerId, status);

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
}

