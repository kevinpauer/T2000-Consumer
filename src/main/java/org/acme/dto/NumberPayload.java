package org.acme.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NumberPayload {

  Double number1;
  Double number2;
  Integer id;

  public NumberPayload(Integer id, Double number1, Double number2) {
    this.id = id;
    this.number1 = number1;
    this.number2 = number2;
  }
}
