package org.acme.consumer;

import io.quarkus.runtime.StartupEvent;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import org.acme.dto.NumberPayload;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

public class PayloadConsumer {

  @Inject
  Logger logger;
  private Timestamp creationTimestamp;
  private Timestamp terminationTimestemp;
  private Random random = new Random();

  public void onStart(@Observes final StartupEvent ev) {
    Date date = new Date();
    creationTimestamp = new Timestamp(date.getTime());
  }


  @Incoming("numbers-payload")
  public void newPayload(NumberPayload payload) {
    Double[] numbersArray = payload.getNumbersList();
    Date date = new Date();
    logger.info("Start arithmetic operation at: " + new Timestamp(date.getTime()));
    doArithmeticOperation(numbersArray);
    date = new Date();
    logger.info("End arithmetic operation at: " + new Timestamp(date.getTime()));
  }

  private void doArithmeticOperation(Double[] numbersArray) {
    for (int i = 0; i < numbersArray.length; i++) {
      for (int j = 0; j < 750; j++) {
        numbersArray[i] = numbersArray[i] + 0 + random.nextDouble() * 1000000;
      }
    }
    Arrays.sort(numbersArray);
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