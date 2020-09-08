package com.intetics.test.service.analyzer.validator.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.service.analyzer.validator.SymbolValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.mockito.Mockito.doReturn;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/07/2020 1:02
 *
 * @author d.velichkevich
 */
public class BracketOpeningValidatorTest extends AbstractNameSymbolsAwareValidatorTestCase<BracketOpeningValidator> {

    @Override
    protected BracketOpeningValidator createValidator() {
        return new BracketOpeningValidator(getValidatorMock());
    }

    @Override
    protected char getExpectedTargetSymbol() {
        return SymbolValidator.BRACKET_OPENING;
    }

    @Test
    public void testIsValidWhenIndexZero() throws Exception {
        // Given
        doReturn(0).when(getContextMock()).getIndex();

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isTrue();

        getOrder().verify(getContextMock()).getIndex();
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testIsValidWhenIndexLastIndex() throws Exception {
        // Given
        mockTillLastIndexCall(3, 3);

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isFalse();

        verifyTillLastIndexCall();
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testIsValidWhenPreviousSymbolsQuote() throws Exception {
        // Given
        mockTillTextCall(1, 7, "\"(test)\"");

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isTrue();

        verifyTillTextCall();
        getOrder().verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @CsvSource(value = {
        "5;11;\"test(test)\";t;true;true;t",
        "5;11;test((test));(;false;false;("
    }, delimiter = ';')
    public void testIsValidWhenAgainstPreviousSymbolValidNameSymbol() throws Exception {
        // Given
        mockTillTextCall(5, 11, "\"test(test)\"");
        AnalyzingContext newContext = mockPreviousSymbolValidNameSymbol('t', true);

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isTrue();

        verifyTillTextCall();
        verifyPreviousSymbolValidNameSymbol('t', newContext);
    }

    private void mockTillTextCall(int index, int lastIndex, String text) {
        mockTillLastIndexCall(index, lastIndex);
        doReturn(text).when(getContextMock()).getText();
    }

    private void mockTillLastIndexCall(int index, int lastIndex) {
        doReturn(index).when(getContextMock()).getIndex();
        doReturn(lastIndex).when(getContextMock()).getLastIndex();
    }

    private void verifyTillTextCall() {
        verifyTillLastIndexCall();
        getOrder().verify(getContextMock()).getText();
    }

    private void verifyTillLastIndexCall() {
        getOrder().verify(getContextMock()).getIndex();
        getOrder().verify(getContextMock()).getLastIndex();
    }
}
