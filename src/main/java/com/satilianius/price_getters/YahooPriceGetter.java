package com.satilianius.price_getters;

import com.satilianius.models.Price;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.ZonedDateTime;
import java.util.List;

public class YahooPriceGetter {

    public static String test() {

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-chart?interval=1d&symbol=DBK.DE&range=5d"))
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

    List<Price> getPrices(String symbol, ZonedDateTime start, ZonedDateTime end, Interval interval) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://apidojo-yahoo-finance-v1.p.rapidapi.com/stock/v2/get-chart?interval=5m&symbol=AMRN&range=1d&region=US"))
                .header("x-rapidapi-key", "7fbad85a5cmsh7c115919f64a4bdp1f004ajsn4c3510edf43f")
                .header("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            return getPricesFromResponse(response.body());

        } catch (IOException | java.lang.InterruptedException e) {
            System.out.println("Couldn't reach API");
            return null;
        }
    }

    private List<Price> getPricesFromResponse(String response) {

    }
}
