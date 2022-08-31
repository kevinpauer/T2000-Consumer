package org.acme.dto;

import io.quarkus.runtime.annotations.RegisterForReflection;
import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@RegisterForReflection
public class ResultPayload {

  Integer id;
  List<Double> numbers;
  Timestamp timestamp;
  Result result;

  public ResultPayload(Integer id, List<Double> numbers, Timestamp timestamp,
      Result results) {
    this.id = id;
    this.numbers = numbers;
    this.timestamp = timestamp;
    this.result = results;
  }
}
