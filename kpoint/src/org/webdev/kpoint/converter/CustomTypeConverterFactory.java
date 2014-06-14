package org.webdev.kpoint.converter;

import java.sql.Time;

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.validation.DefaultTypeConverterFactory;

public class CustomTypeConverterFactory extends DefaultTypeConverterFactory {
    @Override
    public void init(Configuration configuration) {
        super.init(configuration);
        add(Time.class, TimeConverter.class);
    }
}
