package com.cyecize.multidb.areas.conn.converters;

import com.cyecize.multidb.areas.conn.enums.DatabaseProvider;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class StringToDbProviderAdapter implements Converter<String, DatabaseProvider> {

    @Override
    public DatabaseProvider convert(String source) {
        return Arrays.stream(DatabaseProvider.values())
                .filter(e -> e.name().equals(source))
                .findFirst().orElse(null);
    }
}
