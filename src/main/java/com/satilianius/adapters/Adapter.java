package com.satilianius.adapters;

import com.satilianius.db.DbAccessor;
import com.satilianius.models.Note;
import com.satilianius.models.Price;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
        return dbAccessor.getPrices(symbol, startTime, endTime);
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
}
