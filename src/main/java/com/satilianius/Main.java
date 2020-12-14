package com.satilianius;

import com.satilianius.adapters.Adapter;
import com.satilianius.db.DbAccessor;
import com.satilianius.models.Interval;
import com.satilianius.models.Note;
import com.satilianius.price_getters.YahooPriceGetter;
import com.satilianius.views.ChartView;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Set;

public class Main {
    public static void main(String[] args){
        DbAccessor dbAccessor = new DbAccessor();

        Note note = getTestNote();

        System.out.println("adding a note");
        dbAccessor.addNote(note);

        System.out.println("Querying the note history data");
        YahooPriceGetter priceGetter = new YahooPriceGetter();
        note.getSymbols().stream()
                .map(symbol -> priceGetter.getClosingPrices(
                        symbol,
                        note.getStartTime(),
                        note.getEndTime(),
                        Interval.ONE_DAY))
                .flatMap(Collection::stream)
                .forEach(dbAccessor::addPrice);

        Adapter adapter = new Adapter(dbAccessor);
        ChartView view = new ChartView(adapter);
        view.initialiseWindow(note);

        System.out.println("removing a note");
        dbAccessor.removeNote(note);
    }

    private static Note getTestNote() {
        Note note = new Note();
        note.setStartTime(ZonedDateTime.of(2018, 1, 10, 0, 0, 0, 0, ZoneId.of("Europe/Moscow")));
        note.setEndTime(ZonedDateTime.of(2021, 1, 10, 0, 0, 0, 0, ZoneId.of("Europe/Moscow")));
        note.setSymbols(Set.of("ADS.DE", "VOW3.DE", "LHA.DE", "DBK.DE"));
        note.setCouponBarrier(0.8);
        note.setCapitalProtectionBarrier(0.6);
        return note;
    }
}
