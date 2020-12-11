package com.satilianius;

import com.satilianius.db.DbAccessor;
import com.satilianius.models.Note;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

public class Main {
    public static void main(String[] args){
        DbAccessor dbAccessor = new DbAccessor();

        ZonedDateTime now = ZonedDateTime.now();
        Note note = new Note();
        note.setStartTime(now);
        note.setEndTime(now.plusYears(1));
        note.setSymbols(Set.of());
        note.setCouponBarrier(0.6);
        note.setCapitalProtectionBarrier(0.4);

        System.out.println("adding a note");
        dbAccessor.addNote(note);

        List<Note> notes = dbAccessor.getAllNotes();
        System.out.println(notes);

        System.out.println("removing a note");
        dbAccessor.removeNote(note);

        notes = dbAccessor.getAllNotes();
        System.out.println(notes);

    }
}
