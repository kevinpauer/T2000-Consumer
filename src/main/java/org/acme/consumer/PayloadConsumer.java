package org.acme.consumer;

import io.quarkus.runtime.StartupEvent;
import io.smallrye.reactive.messaging.kafka.KafkaRecord;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.acme.dto.NumberPayload;
import org.acme.dto.Result;
import org.acme.dto.ResultPayload;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

public class PayloadConsumer {

  @Inject
  Logger logger;

  @Inject
  @Channel("result-topic")
  Emitter<ResultPayload> resultEmitter;

  private Timestamp creationTimestamp;
  private Random random = new Random();
  private List<Double> resultArray;

  private static boolean isSorted(List<Double> a) {
    if (a == null) {
      return false;
    } else if (a.isEmpty()) {
      return true;
    }
    for (int i = 0; i < a.size() - 1; i++) {
      if (a.get(i) > a.get(i + 1)) {
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
    try {
      logger.info("payload: " + payload);
      logger.info("payload: " + payload.getPayload());
      List<Double> numbersArray = payload.getPayload().getNumbersList();
      logger.info(payload.getTimestamp());
      Date date = new Date();
      logger.info("Start arithmetic operation at: " + new Timestamp(date.getTime()));
      resultArray = doArithmeticOperation(numbersArray);
      date = new Date();
      logger.info("End arithmetic operation at: " + new Timestamp(date.getTime()));
      sendResultToChannel(payload.getPayload());
    } catch (Exception ex) {
      logger.warn("newPayload failed!");
    }

    return payload.ack();
  }

  private List<Double> doArithmeticOperation(List<Double> numbersArray) {
    for (int i = 0; i < numbersArray.size(); i++) {
      for (int j = 0; j < 750; j++) {
        numbersArray.set(i, numbersArray.get(i) * -1);
      }
    }
    return numbersArray.stream().sorted().collect(Collectors.toList());
  }

  private void sendResultToChannel(NumberPayload incomingPayloadData) {
    Date date = new Date();
    Result result = new Result(1, new Timestamp(date.getTime()), isSorted(resultArray));
    ResultPayload resultPayload = new ResultPayload(incomingPayloadData.getId(), incomingPayloadData.getNumbersList(), incomingPayloadData.getTimestamp(), result);
    resultEmitter.send(resultPayload);
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