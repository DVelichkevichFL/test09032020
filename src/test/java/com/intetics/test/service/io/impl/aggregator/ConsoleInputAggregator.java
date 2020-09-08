package com.intetics.test.service.io.impl.aggregator;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/04/2020 4:24
 *
 * @author d.velichkevich
 */
public class ConsoleInputAggregator implements ArgumentsAggregator {

    private static final String ARGUMENT_NEW_LINE = "\n";


    @Override
    public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
        String result = argumentsAccessor.getString(parameterContext.getIndex());
        return (null != result) ? (result) : (ARGUMENT_NEW_LINE);
    }
}
