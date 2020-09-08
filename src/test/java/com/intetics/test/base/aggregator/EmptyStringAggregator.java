package com.intetics.test.base.aggregator;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/04/2020 16:29
 *
 * @author d.velichkevich
 */
public class EmptyStringAggregator implements ArgumentsAggregator {

    private static final String STRING_EMPTY = "";
    private static final String STRING_SPACE = " ";


    @Override
    public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
        String result = argumentsAccessor.getString(parameterContext.getIndex());
        return (null == result) ? (STRING_EMPTY) : (result + STRING_SPACE);
    }
}
