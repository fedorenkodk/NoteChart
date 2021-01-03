package com.satilianius.deserializers;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.satilianius.models.QuoteHistory;

import java.io.IOException;
import java.util.List;

public class YahooChartDeserializer extends StdDeserializer<QuoteHistory> {

    public YahooChartDeserializer() {
        this(null);
    }

    public YahooChartDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public QuoteHistory deserialize(JsonParser jsonParser, DeserializationContext context)
            throws IOException {
        QuoteHistory quoteHistory = new QuoteHistory();

        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        JsonNode result = root.get("chart").get("result").get(0);
        JsonNode meta = result.get("meta");
        JsonNode quoteIndicators = result.get("indicators").get("quote").get(0);

        quoteHistory.setSymbol(meta.get("symbol").textValue());
        quoteHistory.setTimeZone(meta.get("timezone").textValue());

        ObjectMapper mapper = new ObjectMapper();
        ObjectReader reader = mapper.readerFor(new TypeReference<List<Long>>(){});
        quoteHistory.setTimestamps(reader.readValue(result.get("timestamp")));

        reader = mapper.readerFor(new TypeReference<List<Double>>(){});
        quoteHistory.setOpenPrices(reader.readValue(quoteIndicators.get("open")));
        quoteHistory.setClosePrices(reader.readValue(quoteIndicators.get("close")));
        quoteHistory.setHighPrices(reader.readValue(quoteIndicators.get("high")));
        quoteHistory.setLowPrices(reader.readValue(quoteIndicators.get("low")));

        return quoteHistory;
    }
}