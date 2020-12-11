package com.satilianius.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Price {

    @Id
    @GeneratedValue
    Integer id;
    String symbol;
    Long timestamp;
    Double openPrice;
    Double closePrice;
    Double HighPrice;
    Double LowPrice;
}
