package com.satilianius.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.satilianius.deserializers.YahooChartDeserializer;
import lombok.Data;

import java.util.List;

@Data
@JsonDeserialize(using = YahooChartDeserializer.class)
public class QuoteHistory {
    String symbol;
    String timeZone;
    List<Long> timestamps;
    List<Double> openPrices;
    List<Double> closePrices;
    List<Double> highPrices;
    List<Double> lowPrices;

    List<Price> prices;

    public List<Price> getPrices() {
        if (prices == null) {
            for (int i = 0; i < timestamps.size(); i++) {
                Price price = new Price();
                price.setSymbol(symbol);
                price.setTimezone(timeZone);
                price.setTimestamp(timestamps.get(i));
                price.setOpenPrice(openPrices.get(i));
                price.setClosePrice(closePrices.get(i));
                price.setHighPrice(highPrices.get(i));
                price.setLowPrice(lowPrices.get(i));

                prices.add(price);
            }
        }
        return prices;
    }

    @Override
    public String toString() {
        return "QuoteHistory{" +
                "symbol='" + symbol + '\'' +
                ", timeZone='" + timeZone + '\'' +
                ", close='" + closePrices.toString() + '}';
    }
}
