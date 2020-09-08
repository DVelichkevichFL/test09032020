package com.intetics.test.service.impl.aggregator;

import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.model.analyzer.AnalyzingStringErrorEnum;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/08/2020 3:28
 *
 * @author d.velichkevich
 */
public class ParsingErrorAggregator implements ArgumentsAggregator {

    private static final char SYMBOL_ZERO = '\u0000';
    private static final char SYMBOL_NEW_LINE = '\n';

    private static final int INDEX_ERROR_TYPE = 1;
    private static final int INDEX_UNEXPECTED_SYMBOL = 2;
    private static final int INDEX_UNEXPECTED_SYMBOL_INDEX = 3;
    private static final int INDEX_INVALID_END_SYMBOL_INDEX = 4;

    @Override
    public Object aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
        AnalyzingStringErrorEnum errorType = AnalyzingStringErrorEnum.valueOf(argumentsAccessor.getString(INDEX_ERROR_TYPE));
        Character unexpectedSymbol = argumentsAccessor.getCharacter(INDEX_UNEXPECTED_SYMBOL);
        unexpectedSymbol = (SYMBOL_NEW_LINE == unexpectedSymbol) ? (SYMBOL_ZERO) : (unexpectedSymbol);
        Integer unexpectedSymbolIndex = argumentsAccessor.getInteger(INDEX_UNEXPECTED_SYMBOL_INDEX);
        Integer invalidEndSymbolIndex = argumentsAccessor.getInteger(INDEX_INVALID_END_SYMBOL_INDEX);
        return new AnalyzingErrorResult(unexpectedSymbol, unexpectedSymbolIndex, invalidEndSymbolIndex, errorType);
    }
}
