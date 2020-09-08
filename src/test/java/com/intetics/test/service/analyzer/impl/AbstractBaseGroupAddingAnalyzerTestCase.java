package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.StringGroupMock;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.analyzer.AbstractBaseSymbolAnalyzerTestCase;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/07/2020 4:57
 *
 * @author d.velichkevich
 *
 * @param <T> &lt;{@link AbstractBaseGroupAddingAnalyzer}&gt; type of the analyzer to test
 */
public abstract class AbstractBaseGroupAddingAnalyzerTestCase<T extends AbstractBaseGroupAddingAnalyzer> extends AbstractBaseSymbolAnalyzerTestCase<T> {

    @Test
    public void testGettingTargetSymbol() throws Exception {
        // Given - When
        char actual = getAnalyzer().getTargetSymbol();

        // Then
        assertThat(actual).isEqualTo(getExpectedTargetSymbol());
    }

    /**
     * Returns the expected target symbol for this analyzer
     *
     * @return {@link Character} (primitive) value
     */
    protected abstract char getExpectedTargetSymbol();

    @Test
    public void testAnalyzingForNotSupportedSymbol() throws Exception {
        // Given
        mockTillSymbolCall('t');
        mockAnalyzingNextReturningNull();

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isNull();

        verifyTillSymbolCall();
        verifyAnalyzingNextReturningNull();
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testAnalyzingForNotSupportedSymbolWhenIndexLessThanLastIndex() throws Exception {
        // Given
        mockTillSymbolCall(getExpectedTargetSymbol());
        mockTillLastIndexCall(1, 3);

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isNull();

        verifyTillSymbolCall();
        verifyTillLastIndexCall();
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testAnalyzingForNotSupportedSymbolWhenIndexEqualLastIndex() throws Exception {
        // Given
        mockTillSymbolCall(getExpectedTargetSymbol());
        mockTillLastIndexCall(3, 3);
        mockAnalyzingNextReturningNull();

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isNull();

        verifyTillSymbolCall();
        verifyTillLastIndexCall();
        verifyAnalyzingNextReturningNull();
        getOrder().verifyNoMoreInteractions();
    }

    private void mockTillLastIndexCall(int index, int lastIndex) {
        doNothing().when(getAnalyzer()).addNewGroup(getContextMock());
        doNothing().when(getContextMock()).setTokenName(any(StringBuilder.class));
        doReturn(index).when(getContextMock()).getIndex();
        doReturn(lastIndex).when(getContextMock()).getLastIndex();
    }

    private void mockTillSymbolCall(char symbol) {
        doReturn(getExpectedTargetSymbol()).when(getAnalyzer()).getTargetSymbol();
        doReturn(symbol).when(getContextMock()).getSymbol();
    }

    private void mockAnalyzingNextReturningNull() {
        doReturn(null).when(getAnalyzer()).analyzeNext(getContextMock());
    }

    private void verifyTillLastIndexCall() {
        getOrder().verify(getAnalyzer()).addNewGroup(getContextMock());
        getOrder().verify(getContextMock()).setTokenName(any(StringBuilder.class));
        getOrder().verify(getContextMock()).getIndex();
        getOrder().verify(getContextMock()).getLastIndex();
    }

    private void verifyTillSymbolCall() {
        getOrder().verify(getAnalyzer()).getTargetSymbol();
        getOrder().verify(getContextMock()).getSymbol();
    }

    private void verifyAnalyzingNextReturningNull() {
        getOrder().verify(getAnalyzer()).analyzeNext(getContextMock());
    }

    protected void mockTillAddingCurrentLevelGroupCall(StringGroupMock groupMock, int bracketsCount, int newBracketsCount) {
        doReturn(bracketsCount).when(getContextMock()).getBracketsCount();
        doNothing().when(getContextMock()).setBracketsCount(newBracketsCount);
        doReturn(groupMock).when(getAnalyzer()).addCurrentLevelGroup(getContextMock(), getStrategyMock());
    }

    protected void verifyTillAddingCurrentLevelGroupCall(int expectedNewBracketsCount) {
        getOrder().verify(getContextMock()).getBracketsCount();
        getOrder().verify(getContextMock()).setBracketsCount(expectedNewBracketsCount);
        getOrder().verify(getAnalyzer()).addCurrentLevelGroup(getContextMock(), getStrategyMock());
    }
}
