package com.satilianius.models;

import lombok.Data;

import java.time.ZonedDateTime;
import java.util.List;

@Data
public class Note {
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private List<String> symbols;
    private double couponBarrier;
    private double capitalProtectionBarrier;
}