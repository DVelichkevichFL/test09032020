package com.intetics.test.service.analyzer.impl;

import com.intetics.test.service.analyzer.validator.SymbolValidator;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doReturn;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/07/2020 13:26
 *
 * @author d.velichkevich
 */
public class CommaAnalyzerTest extends AbstractBaseGroupAddingAnalyzerTestCase<CommaAnalyzer> {

    @Override
    protected char getExpectedTargetSymbol() {
        return SymbolValidator.COMMA;
    }

    @Override
    protected CommaAnalyzer createAnalyzer() {
        return new CommaAnalyzer(getNextMock(), getStrategyMock());
    }

    @Override
    protected CommaAnalyzer createAnalyzerWithNullNext() {
        return new CommaAnalyzer(null, getStrategyMock());
    }

    @Test
    public void testAddingNewGroup() throws Exception {
        // Given
        doReturn(null).when(getAnalyzer()).addCurrentLevelGroup(getContextMock(), getStrategyMock());

        // When
        getAnalyzer().addNewGroup(getContextMock());

        // Then
        getOrder().verify(getAnalyzer()).addCurrentLevelGroup(getContextMock(), getStrategyMock());
        getOrder().verifyNoMoreInteractions();
    }
}
