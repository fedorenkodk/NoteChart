package com.satilianius.models;

public enum Interval {
    ONE_MINUTE("1m"),
    TWO_MINUTES("2m"),
    FIVE_MINUTES("5m"),
    FIFTEEN_MINUTES("15m"),
    ONE_HOUR("60m"),
    ONE_DAY("1d");

    public final String endpointString;

    Interval(String endpointString) {
        this.endpointString = endpointString;
    }
}