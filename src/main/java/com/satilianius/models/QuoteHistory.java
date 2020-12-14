package com.satilianius.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;


public class QuoteHistory {
    @JsonProperty("chart.result[0].meta.symbol")
    String symbol;
    @JsonProperty("chart.result[0].meta.timezone")
    String timeZone;
    @JsonProperty("chart.result[0].timestamps")
    List<Long> timestamps;
    @JsonProperty("chart.result[0].indicators.quote[0].open")
    List<Double> openPrices;
    @JsonProperty("chart.result[0].indicators.quote[0].close")
    List<Double> closePrices;
    @JsonProperty("chart.result[0].indicators.quote[0].high")
    List<Double> highPrices;
    @JsonProperty("chart.result[0].indicators.quote[0].low")
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
}
