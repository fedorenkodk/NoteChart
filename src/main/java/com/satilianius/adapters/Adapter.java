package com.satilianius.adapters;

import com.satilianius.db.DbAccessor;
import com.satilianius.models.Interval;
import com.satilianius.models.Note;
import com.satilianius.models.Price;
import com.satilianius.price_getters.YahooPriceGetter;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Retrieves the data from the Model and Converts it to the format required by the View.
 * In this case it doesn't need to know about view, it just provides the methods to retrieve the data from Model
 */
public class Adapter {
    private final DbAccessor dbAccessor;

    public Adapter(DbAccessor dbAccessor) {
        this.dbAccessor = dbAccessor;
    }

    public List<Price> getPrices(String symbol, ZonedDateTime startTime, ZonedDateTime endTime) {
        List<Price> savedPrices = dbAccessor.getPrices(symbol, startTime, endTime);
        Price latestSavedPrice = savedPrices.stream()
                .max(Comparator.comparingLong(Price::getTimestamp))
                .orElse(null);

        if (latestSavedPrice != null && latestSavedPrice.getTimestamp() != null) {

            ZonedDateTime latestSavedTime = getZonedDateTime(latestSavedPrice).truncatedTo(ChronoUnit.DAYS);
            ZonedDateTime today = ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS);

            if (latestSavedTime.isBefore(today)) {
                updateSymbolInDb(symbol, latestSavedTime, today);
                return dbAccessor.getPrices(symbol, startTime, endTime);
            } else { // prices are up to date
                return savedPrices;
            }
        } // Prices are not present
        updateSymbolInDb(symbol, startTime, endTime);
        return dbAccessor.getPrices(symbol, startTime, endTime);
    }

    private ZonedDateTime getZonedDateTime(Price latestSavedPrice) {
        return ZonedDateTime.of(
                LocalDateTime.ofEpochSecond(
                        latestSavedPrice.getTimestamp(),
                        0,
                        ZoneOffset.UTC),
                TimeZone.getTimeZone(latestSavedPrice.getTimezone()).toZoneId());
    }

    public XYDataset getNoteDataSet(Note note) {
        TimeSeriesCollection dataSet = new TimeSeriesCollection();
        for (String symbol : note.getSymbols()) {
            final TimeSeries history = new TimeSeries("Relative Historical data");

            List<Price> histData = getPrices(symbol, note.getStartTime(), note.getEndTime());

            // TODO convert to relative prices
//            Double firstPrice = histData.get(0).getClosePrice();
//            List<Double> relativePrices = histData.stream()
//                    .map(price -> price.getClosePrice() / firstPrice)
//                    .collect(Collectors.toList());

            histData.forEach(price -> history.add(
                    new Day(new Date(price.getTimestamp() * 1000)),
                    price.getClosePrice()));
            dataSet.addSeries(history);
        }
        return dataSet;
    }

    void updateSymbolInDb(String symbol, ZonedDateTime startTime, ZonedDateTime endTime) {
        YahooPriceGetter priceGetter = new YahooPriceGetter();
        List<Price> newPrices = priceGetter.getPrices(
                        symbol,
                        startTime,
                        endTime,
                        Interval.ONE_DAY);
        newPrices.forEach(dbAccessor::addPrice);
    }

    void updateNoteInDb() {

    }
}
