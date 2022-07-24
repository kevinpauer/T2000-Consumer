package org.acme.consumer;

import io.quarkus.runtime.StartupEvent;
import java.sql.Timestamp;
import java.util.Date;
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

  public void onStart(@Observes final StartupEvent ev) {
    Date date = new Date();
    creationTimestamp = new Timestamp(date.getTime());
  }

  @Incoming("numbers-payload")
  public void newPayload(NumberPayload payload) {
    logger.infov("Result {0}: {1}", payload.getId(), payload.getNumber1() + payload.getNumber2());
  }
}
