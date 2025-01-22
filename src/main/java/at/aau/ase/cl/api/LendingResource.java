package at.aau.ase.cl.api;

import at.aau.ase.cl.api.interceptor.exceptions.IllegalMeetingException;
import at.aau.ase.cl.api.interceptor.exceptions.IllegalStatusException;
import at.aau.ase.cl.api.interceptor.exceptions.InvalidOwnerReaderException;
import at.aau.ase.cl.api.model.LendingHistoryModel;
import at.aau.ase.cl.api.model.LendingMeetingModel;
import at.aau.ase.cl.api.model.LendingModel;
import at.aau.ase.cl.api.model.LendingStatus;
import at.aau.ase.cl.mapper.LendingHistoryMapper;
import at.aau.ase.cl.mapper.LendingMapper;
import at.aau.ase.cl.model.LendingEntity;
import at.aau.ase.cl.model.LendingHistoryEntity;
import at.aau.ase.cl.service.LendingService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Path("/lendings")
@Produces(MediaType.APPLICATION_JSON)
public class LendingResource {
    @Inject
    LendingService lendingService;

    @POST
    @Path("/")
    public Response createLending(LendingModel lendingModel) {
        if (lendingModel.getOwnerId().equals(lendingModel.getReaderId())) {
            throw new InvalidOwnerReaderException("Owner cannot create lending request for their own book");
        }

        LendingModel createdLending = lendingService.createLending(lendingModel);

        return Response.ok(createdLending).build();
    }

    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LendingModel.class))})
    public Response getLending(@PathParam("id") UUID id) {
        LendingEntity lending = lendingService.getLendingById(id);
        LendingModel model = LendingMapper.INSTANCE.map(lending);
        return Response.ok(model).build();
    }

    @GET
    @Path("/readers/{readerId}")
    @APIResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LendingModel.class))})
    public Response getLendingsByReaderId(@PathParam("readerId") UUID readerId,
                                          @QueryParam("status") String status) {
        List<LendingEntity> lendings;

        if (status == null) {
            lendings = lendingService.getLendingsByReaderId(readerId);
        } else {
            LendingStatus lendingStatus = validateStatus(status);
            lendings = lendingService.getLendingsByReaderIdAndStatus(readerId, lendingStatus);
        }

        List<LendingModel> models = lendings.stream()
                .map(LendingMapper.INSTANCE::map)
                .toList();
        return Response.ok(models).build();
    }

    @GET
    @Path("/owners/{ownerId}")
    @APIResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LendingModel.class))})
    public Response getLendingsByOwner(@PathParam("ownerId") UUID ownerId) {
        List<LendingEntity> lendings = lendingService.getLendingsByOwnerId(ownerId);
        List<LendingModel> models = lendings.stream()
                .map(LendingMapper.INSTANCE::map)
                .toList();
        return Response.ok(models).build();
    }

    @PATCH
    @Path("/{id}")
    @APIResponse(responseCode = "200", description = "OK")
    public Response updateLendingStatus(@PathParam("id") UUID id,
                                        @QueryParam("status") String status) {
        LendingStatus lendingStatus = validateStatus(status);

        LendingModel lendingModel = lendingService.updateLendingStatus(id, lendingStatus);

        return Response.ok(lendingModel).build();
    }

    @POST
    @Path("/{id}/meeting")
    @APIResponse(responseCode = "200", description = "OK")
    public Response createLendingMeeting(@PathParam("id") UUID lendingId,
                                         @Valid LendingMeetingModel lendingMeetingModel) {
        validateMeeting(lendingMeetingModel);
        LendingModel updatedLendingModel = lendingService.createLendingMeeting(lendingMeetingModel, lendingId);

        return Response.ok(updatedLendingModel).build();
    }

    @GET
    @Path("/{id}/history")
    @APIResponse(responseCode = "200", description = "OK", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = LendingHistoryModel.class))
    })
    public Response getLendingHistory(@PathParam("id") UUID lendingId) {
        List<LendingHistoryEntity> historyEntities = lendingService.getLendingHistoryByLendingId(lendingId);
        List<LendingHistoryModel> historyModels = historyEntities.stream()
                .map(LendingHistoryMapper.INSTANCE::map)
                .toList();

        return Response.ok(historyModels).build();
    }

    private LendingStatus validateStatus(String status) {
        if (status == null || status.isBlank()) {
            throw new IllegalStatusException("Status parameter is required.");
        }
        try {
            return LendingStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalStatusException("Invalid status: " + status + ". Valid statuses are: " +
                    Arrays.toString(LendingStatus.values()));
        }
    }

    private void validateMeeting(LendingMeetingModel meeting) {
        if (meeting.getMeetingLocation() == null || meeting.getMeetingLocation().isBlank()) {
            throw new IllegalMeetingException("Meeting place parameter is required.");
        }
        if (meeting.getMeetingTime() == null) {
            throw new IllegalMeetingException("Meeting time parameter is required.");
        }
        if (meeting.getDeadline() == null) {
            throw new IllegalMeetingException("Deadline parameter is required.");
        }
        if (meeting.getDeadline().isBefore(Instant.now()) || meeting.getMeetingTime().isBefore(Instant.now())) {
            throw new IllegalMeetingException("Dates have to be in the future.");
        }
    }
}
