package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.analyzer.AbstractBaseSymbolAnalyzerTestCase;
import com.intetics.test.service.analyzer.validator.SymbolValidator;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/07/2020 16:14
 *
 * @author d.velichkevich
 */
public class QuoteAnalyzerTest extends AbstractBaseSymbolAnalyzerTestCase<QuoteAnalyzer> {

    @Override
    protected QuoteAnalyzer createAnalyzer() {
        return new QuoteAnalyzer(getNextMock());
    }

    @Override
    protected QuoteAnalyzer createAnalyzerWithNullNext() {
        return new QuoteAnalyzer(null);
    }

    @Test
    public void testAnalyzingForNotSupportedSymbol() throws Exception {
        // Given
        doReturn('t').when(getContextMock()).getSymbol();
        doReturn(null).when(getAnalyzer()).analyzeNext(getContextMock());

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isNull();

        getOrder().verify(getContextMock()).getSymbol();
        getOrder().verify(getAnalyzer()).analyzeNext(getContextMock());
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testAnalyzing() throws Exception {
        // Given
        mockTillLastIndexCall(SymbolValidator.QUOTE, 1);

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isNull();

        verifyTillLastIndexCall();
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testAnalyzingForLstIndex() throws Exception {
        // Given
        mockTillLastIndexCall(SymbolValidator.QUOTE, 2);
        doReturn(null).when(getAnalyzer()).analyzeNext(getContextMock());

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isNull();

        verifyTillLastIndexCall();
        getOrder().verify(getAnalyzer()).analyzeNext(getContextMock());
        getOrder().verifyNoMoreInteractions();
    }

    private void mockTillLastIndexCall(char symbol, int index) {
        doReturn(symbol).when(getContextMock()).getSymbol();
        doReturn(2).when(getContextMock()).getAvailableQuotes();
        doNothing().when(getContextMock()).setAvailableQuotes(1);
        doReturn(index).when(getContextMock()).getIndex();
        doReturn(2).when(getContextMock()).getLastIndex();
    }

    private void verifyTillLastIndexCall() {
        getOrder().verify(getContextMock()).getSymbol();
        getOrder().verify(getContextMock()).getAvailableQuotes();
        getOrder().verify(getContextMock()).setAvailableQuotes(1);
        getOrder().verify(getContextMock()).getIndex();
        getOrder().verify(getContextMock()).getLastIndex();
    }
}
