package org.acme.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class NumberPayload {

  Timestamp timestamp;
  List<Double> numbersList;
  Integer id;
  Integer payloadSize;
}
