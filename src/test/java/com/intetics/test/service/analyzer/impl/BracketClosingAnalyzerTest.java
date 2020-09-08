package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.StringGroupMock;
import com.intetics.test.service.analyzer.validator.SymbolValidator;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/07/2020 14:46
 *
 * @author d.velichkevich
 */
public class BracketClosingAnalyzerTest extends AbstractBaseGroupAddingAnalyzerTestCase<BracketClosingAnalyzer> {

    @Override
    protected char getExpectedTargetSymbol() {
        return SymbolValidator.BRACKET_CLOSING;
    }

    @Override
    protected BracketClosingAnalyzer createAnalyzer() {
        return new BracketClosingAnalyzer(getNextMock(), getStrategyMock());
    }

    @Override
    protected BracketClosingAnalyzer createAnalyzerWithNullNext() {
        return new BracketClosingAnalyzer(null, getStrategyMock());
    }

    @Test
    public void testAddingNewGroup() throws Exception {
        // Given
        mockTillGettingCurrentGroupCall(new StringGroupMock(0, "test", new StringGroupMock()));
        doNothing().when(getContextMock()).setCurrentGroup(new StringGroupMock());

        // When
        getAnalyzer().addNewGroup(getContextMock());

        // Then
        verifyTillGettingCurrentGroupCall();
        getOrder().verify(getContextMock()).setCurrentGroup(new StringGroupMock());
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testAddingNewGroupWhenCurrentGroupRoot() throws Exception {
        // Given
        mockTillGettingCurrentGroupCall(new StringGroupMock());

        // When
        getAnalyzer().addNewGroup(getContextMock());

        // Then
        verifyTillGettingCurrentGroupCall();
        getOrder().verifyNoMoreInteractions();
    }

    private void mockTillGettingCurrentGroupCall(StringGroupMock currentGroup) {
        mockTillAddingCurrentLevelGroupCall(new StringGroupMock(), 1, 0);
        doReturn(currentGroup).when(getContextMock()).getCurrentGroup();
    }

    private void verifyTillGettingCurrentGroupCall() {
        verifyTillAddingCurrentLevelGroupCall(0);
        getOrder().verify(getContextMock()).getCurrentGroup();
    }
}
