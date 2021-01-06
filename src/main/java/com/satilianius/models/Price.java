package com.satilianius.models;

import lombok.Data;
import lombok.NonNull;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
public class Price {
    @Id
    @GeneratedValue
    Integer id;
    String symbol;
    Long timestamp;
    String timezone;
    Double openPrice;
    Double closePrice;
    Double HighPrice;
    Double LowPrice;
}
