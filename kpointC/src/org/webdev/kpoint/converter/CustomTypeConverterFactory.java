package org.webdev.kpoint.converter;

import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.validation.DefaultTypeConverterFactory;

import org.webdev.kpoint.bl.pojo.State;

public class CustomTypeConverterFactory extends DefaultTypeConverterFactory {
    @Override
    public void init(Configuration configuration) {
        super.init(configuration);
        add(State.class, StateConverter.class);
    }
}
