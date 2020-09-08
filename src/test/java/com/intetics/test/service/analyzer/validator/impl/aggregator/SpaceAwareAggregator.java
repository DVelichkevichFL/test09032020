package com.intetics.test.service.analyzer.validator.impl.aggregator;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/06/2020 19:12
 *
 * @author d.velichkevich
 */
public class SpaceAwareAggregator implements ArgumentsAggregator {

    private static final char SYMBOL_SPACE = ' ';


    @Override
    public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
        Character result = argumentsAccessor.getCharacter(parameterContext.getIndex());
        return (null == result) ? (SYMBOL_SPACE) : (result);
    }
}
