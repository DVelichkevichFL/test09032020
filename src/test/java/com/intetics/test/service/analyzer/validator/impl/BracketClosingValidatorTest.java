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
 * Created : 09/07/2020 2:39
 *
 * @author d.velichkevich
 */
public class BracketClosingValidatorTest extends AbstractNameSymbolsAwareValidatorTestCase<BracketClosingValidator> {

    @Override
    protected BracketClosingValidator createValidator() {
        return new BracketClosingValidator(getValidatorMock());
    }

    @Override
    protected char getExpectedTargetSymbol() {
        return SymbolValidator.BRACKET_CLOSING;
    }

    @Test
    public void testIsValidWhenBracketsZero() throws Exception {
        // Given
        doReturn(0).when(getContextMock()).getBracketsCount();

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isFalse();

        getOrder().verify(getContextMock()).getBracketsCount();
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testIsValidWhenIndexZero() throws Exception {
        // Given
        mockTillIndexCall(0, 2);

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isFalse();

        verifyTillIndexCall();
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testIsValidWhenPreviousSymbolClosingBracket() throws Exception {
        // Given
        mockTillTextCall(20, 2, "test(test,text(test)),test");

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isTrue();

        verifyTillTextCall();
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testIsValidWhenClosingLeadingBracketBeforeStringEnd() throws Exception {
        // Given
        mockTillTextCall(5, 1, "(test),(test,text(test)),test");
        doReturn(28).when(getContextMock()).getLastIndex();

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isFalse();

        verifyTillTextCall();
        getOrder().verify(getContextMock()).getLastIndex();
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testIsValidWithoutLeadingBracket() throws Exception {
        // Given
        mockTillTextCall(21, 1, "test,(test,text(test)),test");
        doReturn(26).when(getContextMock()).getLastIndex();

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isTrue();

        verifyTillTextCall();
        getOrder().verify(getContextMock()).getLastIndex();
        getOrder().verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @CsvSource(value = {
        "19;test(test,text(test)),test;t;true;true;t",
        "21;test(test,text(test)(),test;(;false;false;("
    }, delimiter = ';')
    public void testIsValidAgainstPreviousNameSymbol(
            int index, String text, char previousSymbol, boolean previousSymbolValid, boolean expectedResult, char expectedPreviousSymbol) throws Exception {

        // Given
        mockTillTextCall(index, 2, text);
        AnalyzingContext newContext = mockPreviousSymbolValidNameSymbol(previousSymbol, previousSymbolValid);

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isEqualTo(expectedResult);

        verifyTillTextCall();
        verifyPreviousSymbolValidNameSymbol(expectedPreviousSymbol, newContext);
    }

    private void mockTillTextCall(int index, int bracketsCount, String text) {
        mockTillIndexCall(index, bracketsCount);
        doReturn(text).when(getContextMock()).getText();
    }

    private void mockTillIndexCall(int index, int bracketsCount) {
        doReturn(index).when(getContextMock()).getIndex();
        doReturn(bracketsCount).when(getContextMock()).getBracketsCount();
    }

    private void verifyTillTextCall() {
        verifyTillIndexCall();
        getOrder().verify(getContextMock()).getText();
    }

    private void verifyTillIndexCall() {
        getOrder().verify(getContextMock()).getIndex();
        getOrder().verify(getContextMock()).getBracketsCount();
    }
}
