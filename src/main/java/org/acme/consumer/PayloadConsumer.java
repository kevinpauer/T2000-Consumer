package org.acme.consumer;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import io.smallrye.reactive.messaging.kafka.Record;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.acme.dto.NumberPayload;
import org.acme.dto.Result;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

public class PayloadConsumer {

  @Inject
  Logger logger;

  @Inject
  @Channel("result-topic")
  Emitter<Result> resultEmitter;

  private Timestamp creationTimestamp;
  private Random random = new Random();
  private Double[] resultArray = new Double[50000];

  private static boolean isSorted(Double[] a) {
    if (a == null) {
      return false;
    } else if (a.length == 0) {
      return true;
    }
    for (int i = 0; i < a.length - 1; i++) {
      if (a[i] > a[i + 1]) {
        return false;
      }
    }
    return true;
  }

  public void onStart(@Observes final StartupEvent ev) {
    Date date = new Date();
    creationTimestamp = new Timestamp(date.getTime());
  }

  @Incoming("numbers-payload")
  public CompletionStage<Void> newPayload(KafkaRecord<Integer, NumberPayload> payload) {
    Double[] numbersArray = payload.getPayload().getNumbersList();
    logger.info(payload.getTimestamp());
    Date date = new Date();
    logger.info("Start arithmetic operation at: " + new Timestamp(date.getTime()));
    resultArray = doArithmeticOperation(numbersArray);
    date = new Date();
    logger.info("End arithmetic operation at: " + new Timestamp(date.getTime()));

    sendResultToChannel();

    return CompletableFuture.runAsync(()->{});
  }

  private Double[] doArithmeticOperation(Double[] numbersArray) {
    for (int i = 0; i < numbersArray.length; i++) {
      for (int j = 0; j < 750; j++) {
        numbersArray[i] = numbersArray[i] + 0 + random.nextDouble() * 1000000;
      }
    }
    Arrays.sort(numbersArray);
    return numbersArray;
  }

  private void sendResultToChannel() {
    Date date = new Date();
    Result result = new Result(new Timestamp(date.getTime()), isSorted(resultArray));
    resultEmitter.send(result);
  }
}

// Ergebnis topic erstellen mit den Ergebnissen der Rechenoperation
// Array itterieren
// - Sorting mit großer Liste
// - Output in Ergebnis topic
//
// Kafka schauen wann Event erstellt wurde mit Metadaten
// Kafka Rekord wegen Timestemp
//
// Experiment wenn setup, wie reagieren einzelne deployments auf verschiedene available ressource größen
// Mehr ram etc