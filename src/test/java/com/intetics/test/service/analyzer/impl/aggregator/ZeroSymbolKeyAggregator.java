package com.intetics.test.service.analyzer.impl.aggregator;

import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/07/2020 17:35
 *
 * @author d.velichkevich
 */
public class ZeroSymbolKeyAggregator implements ArgumentsAggregator {

    private static final char KEY_DEFAULT_VALIDATOR = '\u0000';


    @Override
    public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
        Character symbol = argumentsAccessor.getCharacter(parameterContext.getIndex());
        return (null == symbol) ? (KEY_DEFAULT_VALIDATOR) : (symbol);
    }
}
