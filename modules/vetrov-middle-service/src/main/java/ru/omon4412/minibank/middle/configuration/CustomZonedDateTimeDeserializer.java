package ru.omon4412.minibank.middle.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomZonedDateTimeDeserializer extends JsonDeserializer<ZonedDateTime> {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");

    @Override
    public ZonedDateTime deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext)
            throws IOException {
        String dateTimeString = jsonParser.getText();

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ");
            return ZonedDateTime.parse(dateTimeString, formatter);
        } catch (DateTimeParseException e) {
            try {
                double timestampSeconds = Double.parseDouble(dateTimeString);
                long seconds = (long) timestampSeconds;
                long nanos = (long) ((timestampSeconds - seconds) * 1_000_000_000);
                return ZonedDateTime.ofInstant(Instant.ofEpochSecond(seconds, nanos), deserializationContext.getTimeZone().toZoneId());
            } catch (NumberFormatException ex) {
                throw new IOException("Cannot parse timestamp: " + dateTimeString, ex);
            }
        }
    }
}
