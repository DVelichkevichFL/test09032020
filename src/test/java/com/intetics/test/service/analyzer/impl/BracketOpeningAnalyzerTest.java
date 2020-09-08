package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.StringGroupMock;
import com.intetics.test.service.analyzer.validator.SymbolValidator;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doNothing;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/07/2020 14:31
 *
 * @author d.velichkevich
 */
public class BracketOpeningAnalyzerTest extends AbstractBaseGroupAddingAnalyzerTestCase<BracketOpeningAnalyzer> {

    @Override
    protected char getExpectedTargetSymbol() {
        return SymbolValidator.BRACKET_OPENING;
    }

    @Override
    protected BracketOpeningAnalyzer createAnalyzer() {
        return new BracketOpeningAnalyzer(getNextMock(), getStrategyMock());
    }

    @Override
    protected BracketOpeningAnalyzer createAnalyzerWithNullNext() {
        return new BracketOpeningAnalyzer(null, getStrategyMock());
    }

    @Test
    public void testAddingNewGroup() throws Exception {
        // Given
        StringGroupMock groupMock = new StringGroupMock();
        mockTillAddingCurrentLevelGroupCall(groupMock, 0, 1);
        doNothing().when(getContextMock()).setCurrentGroup(groupMock);

        // When
        getAnalyzer().addNewGroup(getContextMock());

        // Then
        verifyTillAddingCurrentLevelGroupCall(1);
        getOrder().verify(getContextMock()).setCurrentGroup(groupMock);
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testAddingNewGroupWhenGroupNotCreated() throws Exception {
        // Given
        mockTillAddingCurrentLevelGroupCall(null, 0, 1);

        // When
        getAnalyzer().addNewGroup(getContextMock());

        // Then
        verifyTillAddingCurrentLevelGroupCall(1);
        getOrder().verifyNoMoreInteractions();
    }

}
