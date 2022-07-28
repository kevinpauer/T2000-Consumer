package org.acme.dto;

import java.sql.Timestamp;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
