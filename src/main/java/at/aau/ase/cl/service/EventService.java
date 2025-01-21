package at.aau.ase.cl.service;

import at.aau.ase.cl.event.LendingEvent;
import at.aau.ase.cl.model.LendingEntity;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class EventService {
    public static final String LENDING_EVENT_CHANNEL = "lending";

    @Channel(LENDING_EVENT_CHANNEL)
    Emitter<LendingEvent> lendingEventEmitter;

    @Transactional(Transactional.TxType.NOT_SUPPORTED)
    public void sendLendingEvent(LendingEntity lending) {
        if (lending == null || lending.getBookId() == null) {
            Log.warnf("Skipping event of inconsistent model: %s", lending);
            return;
        }
        LendingEvent event = new LendingEvent(lending.getBookId(), lending.getOwnerId(), lending.getStatus());
        Log.debugf("Sending lending event: %s", event);
        lendingEventEmitter.send(KafkaRecord.of(event.bookId(), event)
                .withAck(() -> {
                    Log.infof("Lending event sent: %s", event);
                    return CompletableFuture.completedFuture(null);
                }).withNack(e -> {
                    Log.errorf(e, "Failed to send lending event: %s", event);
                    return CompletableFuture.completedFuture(null);
                }));
    }
}
