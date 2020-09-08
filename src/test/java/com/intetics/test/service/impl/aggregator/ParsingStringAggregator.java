package com.intetics.test.service.impl.aggregator;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/08/2020 1:40
 *
 * @author d.velichkevich
 */
public class ParsingStringAggregator implements ArgumentsAggregator {

    private static final String SYMBOL_LINES_DELIMITER = ";";


    @Override
    public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
        String result = argumentsAccessor.getString(parameterContext.getIndex());
        return result.replace(SYMBOL_LINES_DELIMITER, System.lineSeparator());
    }
}
