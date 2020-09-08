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
 * Created : 09/06/2020 15:51
 *
 * @author d.velichkevich
 */
public class QuoteValidatorTest extends AbstractNameSymbolsAwareValidatorTestCase<QuoteValidator> {

    @Override
    protected QuoteValidator createValidator() {
        return new QuoteValidator(getValidatorMock());
    }

    @Override
    protected char getExpectedTargetSymbol() {
        return SymbolValidator.QUOTE;
    }

    @Test
    public void testIsValidWhenIndexIsZero() throws Exception {
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
    public void testIsValidWhenIndexGreaterThanZeroAndAllQuotesAvailable() throws Exception {
        // Given
        doReturn(1).when(getContextMock()).getIndex();
        doReturn(AnalyzingContext.COUNT_MAXIMUM_QUOTES).when(getContextMock()).getAvailableQuotes();

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isFalse();

        getOrder().verify(getContextMock()).getIndex();
        getOrder().verify(getContextMock()).getAvailableQuotes();
        getOrder().verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @CsvSource(value = {
        "7:\"(test),\":false",
        "8:\"(test1)\":true"
    }, delimiter = ':')
    public void testIsValidAgainstLastIndexAndBracket(int index, String text, boolean expectedResult) throws Exception {
        // Given
        mockTillLastIndexCall(index, text);

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isEqualTo(expectedResult);

        verifyTillLastIndexCall();
        getOrder().verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @CsvSource(value = {
        "8:\"_test1,\":,:false:,:false",
        "8:\"_test1_\":_:true:_:true"
    }, delimiter = ':')
    public void testIsValidAgainstLastIndexAndPreviousSymbol(
            int index, String text, char previousSymbol, boolean previousSymbolValid, char expectedPreviousSymbol, boolean expectedResult) throws Exception {

        // Given
        mockTillLastIndexCall(index, text);
        AnalyzingContext newContextMock = mockPreviousSymbolValidNameSymbol(previousSymbol, previousSymbolValid);

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isEqualTo(expectedResult);

        verifyTillLastIndexCall();
        verifyPreviousSymbolValidNameSymbol(expectedPreviousSymbol, newContextMock);
        getOrder().verifyNoMoreInteractions();
    }

    private void mockTillLastIndexCall(int index, String text) {
        doReturn(index).when(getContextMock()).getIndex();
        doReturn(1).when(getContextMock()).getAvailableQuotes();
        doReturn(text).when(getContextMock()).getText();
        doReturn(8).when(getContextMock()).getLastIndex();
    }

    private void verifyTillLastIndexCall() {
        getOrder().verify(getContextMock()).getIndex();
        getOrder().verify(getContextMock()).getAvailableQuotes();
        getOrder().verify(getContextMock()).getText();
        getOrder().verify(getContextMock()).getLastIndex();
    }
}
