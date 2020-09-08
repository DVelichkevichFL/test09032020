package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.analyzer.AbstractBaseSymbolAnalyzerTestCase;
import com.intetics.test.service.analyzer.validator.SymbolValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.mockito.Mockito.doReturn;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/07/2020 15:05
 *
 * @author d.velichkevich
 */
public class DefaultAnalyzerTest extends AbstractBaseSymbolAnalyzerTestCase<DefaultAnalyzer> {

    @Override
    protected DefaultAnalyzer createAnalyzer() {
        return new DefaultAnalyzer(getNextMock());
    }

    @Override
    protected DefaultAnalyzer createAnalyzerWithNullNext() {
        return new DefaultAnalyzer(null);
    }

    @Test
    public void testAnalyzing() throws Exception {
        // Given
        doReturn('t').when(getContextMock()).getSymbol();
        StringBuilder tokenNameMock = new StringBuilder();
        doReturn(tokenNameMock).when(getContextMock()).getTokenName();
        doReturn(null).when(getAnalyzer()).analyzeNext(getContextMock());

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isNull();
        assertThat(tokenNameMock.toString()).isEqualTo("t");

        getOrder().verify(getContextMock()).getSymbol();
        getOrder().verify(getContextMock()).getTokenName();
        getOrder().verify(getAnalyzer()).analyzeNext(getContextMock());
        getOrder().verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @ValueSource(chars = {
        SymbolValidator.QUOTE,
        SymbolValidator.COMMA,
        SymbolValidator.BRACKET_OPENING,
        SymbolValidator.BRACKET_CLOSING
    })
    public void testAnalyzingForSpecialSymbol(char symbol) throws Exception {
        // Given
        doReturn(symbol).when(getContextMock()).getSymbol();
        doReturn(null).when(getAnalyzer()).analyzeNext(getContextMock());

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isNull();

        getOrder().verify(getContextMock()).getSymbol();
        getOrder().verify(getAnalyzer()).analyzeNext(getContextMock());
        getOrder().verifyNoMoreInteractions();
    }
}
