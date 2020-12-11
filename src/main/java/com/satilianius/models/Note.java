package com.satilianius.models;

import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Data
public class Note {
    @Id
    @GeneratedValue
    private Integer noteId;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    @ElementCollection
    @Fetch(FetchMode.JOIN)
    private Set<String> symbols;
    private double couponBarrier;
    private double capitalProtectionBarrier;
}