package com.satilianius.price_getters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.satilianius.models.Interval;
import com.satilianius.models.Price;
import com.satilianius.models.QuoteHistory;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;

public class YahooPriceGetter {

    public List<Price> getPrices(String symbol, ZonedDateTime start, ZonedDateTime end, Interval interval) {
        String response = getResponse(symbol, start, end, interval);
        // writeResponseToFile(response);
        QuoteHistory history = getPricesFromResponse(response);
        return history != null ? history.getPrices() : null;
    }

    private void writeResponseToFile(String response) {
        File file = new File("response.json");
        try(FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos)) {
            //convert string to byte array
            byte[] bytes = response.getBytes();
            //write byte array to file
            bos.write(bytes);
            bos.close();
            fos.close();
            System.out.print("Data written to file successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getResponse(String symbol, ZonedDateTime start, ZonedDateTime end, Interval interval) {
        String endpoint = "https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-chart";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format("%s?interval=%s&symbol=%s&period1=%d&period2=%d",
                        endpoint,
                        interval.endpointString,
                        symbol,
                        getEndpointTime(start),
                        getEndpointTime(end))))
                .header("x-rapidapi-key", "7fbad85a5cmsh7c115919f64a4bdp1f004ajsn4c3510edf43f")
                .header("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();

        } catch (IOException | java.lang.InterruptedException e) {
            System.out.println("Couldn't reach API");
            return null;
        }
    }

    private long getEndpointTime(ZonedDateTime time) {
        return time.toEpochSecond();
    }

    private QuoteHistory getPricesFromResponse(String response) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(response, QuoteHistory.class);
        } catch (JsonProcessingException e) {
            System.out.println("Could not map the response to the history data object: " + e);
            return null;
        }
    }
}
