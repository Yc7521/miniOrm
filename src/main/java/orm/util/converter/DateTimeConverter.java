package orm.util.converter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

public class DateTimeConverter implements Converter<LocalDateTime, String> {
    @Override
    public LocalDateTime parse(String value) {
        return LocalDateTime.parse(value);
    }

    @Override
    public String format(LocalDateTime value) {
        return value.format(new DateTimeFormatterBuilder().parseCaseInsensitive()
                .append(DateTimeFormatter.ISO_LOCAL_DATE).appendLiteral(' ')
                .append(DateTimeFormatter.ISO_LOCAL_TIME).toFormatter());
    }
}
