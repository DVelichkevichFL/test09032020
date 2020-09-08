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
 * Created : 09/06/2020 20:56
 *
 * @author d.velichkevich
 */
public class CommaValidatorTest extends AbstractNameSymbolsAwareValidatorTestCase<CommaValidator> {

    @Override
    protected CommaValidator createValidator() {
        return new CommaValidator(getValidatorMock());
    }

    @Override
    protected char getExpectedTargetSymbol() {
        return SymbolValidator.COMMA;
    }

    @Test
    public void testIsNotValidZeroIndex() throws Exception {
        // Given
        doReturn(0).when(getContextMock()).getIndex();

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        getOrder().verify(getContextMock()).getIndex();
        getOrder().verifyNoMoreInteractions();

        assertThat(actual).isFalse();
    }

    @ParameterizedTest
    @CsvSource(value = {
        "10:test(test),test:14:true",
        "15:test(test),test,:15:false"
    }, delimiter = ':')
    public void testIsValid(int index, String text, int lastIndex, boolean expectedResult) throws Exception {
        // Given
        mockTillLastIndexCall(index, text, lastIndex);

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isEqualTo(expectedResult);

        verifyTillLastIndexCall();
        getOrder().verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @CsvSource(value = {
        "4:test,test,test:13:t:true:true:t",
        "10:test,test(,test:14:(:false:false:("
    }, delimiter = ':')
    public void testIsValidAgainstNameSymbol(
            int index, String text, int lastIndex, char previousSymbol,
            boolean previousSymbolValid, boolean expectedResult, char expectedPreviousSymbol) throws Exception {

        // Given
        mockTillLastIndexCall(index, text, lastIndex);
        AnalyzingContext newContext = mockPreviousSymbolValidNameSymbol(previousSymbol, previousSymbolValid);

        // When
        boolean actual = getValidator().isValid(getContextMock());

        // Then
        assertThat(actual).isEqualTo(expectedResult);

        verifyTillLastIndexCall();
        verifyPreviousSymbolValidNameSymbol(expectedPreviousSymbol, newContext);
        getOrder().verifyNoMoreInteractions();
    }

    private void mockTillLastIndexCall(int index, String text, int lastIndex) {
        doReturn(index).when(getContextMock()).getIndex();
        doReturn(text).when(getContextMock()).getText();
        doReturn(lastIndex).when(getContextMock()).getLastIndex();
    }

    private void verifyTillLastIndexCall() {
        getOrder().verify(getContextMock()).getIndex();
        getOrder().verify(getContextMock()).getText();
        getOrder().verify(getContextMock()).getLastIndex();
    }
}
