package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.StringGroupMock;
import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.model.analyzer.AnalyzingStringErrorEnum;
import com.intetics.test.service.analyzer.AbstractBaseSymbolAnalyzerTestCase;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doReturn;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/07/2020 15:29
 *
 * @author d.velichkevich
 */
public class LastSymbolAnalyzerTest extends AbstractBaseSymbolAnalyzerTestCase<LastSymbolAnalyzer> {

    @Override
    protected LastSymbolAnalyzer createAnalyzer() {
        return new LastSymbolAnalyzer(getNextMock(), getStrategyMock());
    }

    @Override
    protected LastSymbolAnalyzer createAnalyzerWithNullNext() {
        return new LastSymbolAnalyzer(null, getStrategyMock());
    }

    @Test
    @Override
    public void testConstruction() throws Exception {
        // Given - When - Then
        super.testConstruction();

        // Then
        assertThat(getAnalyzer().getStrategy()).isSameAs(getStrategyMock());
    }

    @Test
    public void testAnalyzingWhenNotLastSymbol() throws Exception {
        // Given
        mockTillLastIndexCall(0);
        doReturn(null).when(getAnalyzer()).analyzeNext(getContextMock());

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isNull();

        verifyTillLastIndexCall();
        getOrder().verify(getAnalyzer()).analyzeNext(getContextMock());
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testAnalyzingWhenNotAllBracketsClosed() throws Exception {
        // Given
        mockTillBracketsCountCall(1);

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isEqualTo(new AnalyzingErrorResult(AnalyzingStringErrorEnum.NOT_ALL_GROUPS_CLOSED));

        verifyTillBracketsCountCall();
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testAnalyzingWhenQuoteNotClosed() throws Exception {
        // Given
        mockTillAvailableQuotesCall(1);

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isEqualTo(new AnalyzingErrorResult(AnalyzingStringErrorEnum.FINAL_QUOTATION_MARK_MISSING));

        verifyTillAvailableQuotesCall();
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testAnalyzing() throws Exception {
        // Given
        mockTillAvailableQuotesCall(AnalyzingContext.COUNT_MAXIMUM_QUOTES);
        doReturn(new StringGroupMock()).when(getAnalyzer()).addCurrentLevelGroup(getContextMock(), getStrategyMock());

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isNull();

        verifyTillAvailableQuotesCall();
        getOrder().verify(getAnalyzer()).addCurrentLevelGroup(getContextMock(), getStrategyMock());
        getOrder().verifyNoMoreInteractions();
    }

    private void mockTillAvailableQuotesCall(int availableQuotes) {
        mockTillBracketsCountCall(0);
        doReturn(availableQuotes).when(getContextMock()).getAvailableQuotes();
    }

    private void mockTillBracketsCountCall(int bracketsCount) {
        mockTillLastIndexCall(1);
        doReturn(bracketsCount).when(getContextMock()).getBracketsCount();
    }

    private void mockTillLastIndexCall(int index) {
        doReturn(index).when(getContextMock()).getIndex();
        doReturn(1).when(getContextMock()).getLastIndex();
    }

    private void verifyTillAvailableQuotesCall() {
        verifyTillBracketsCountCall();
        getOrder().verify(getContextMock()).getAvailableQuotes();
    }

    private void verifyTillBracketsCountCall() {
        verifyTillLastIndexCall();
        getOrder().verify(getContextMock()).getBracketsCount();
    }

    private void verifyTillLastIndexCall() {
        getOrder().verify(getContextMock()).getIndex();
        getOrder().verify(getContextMock()).getLastIndex();
    }
}
