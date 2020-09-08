package com.intetics.test.service.analyzer.validator.impl;

import com.intetics.test.base.AbstractBaseMockTestCase;
import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.service.analyzer.validator.impl.aggregator.BooleanValueAggregator;
import com.intetics.test.service.analyzer.validator.impl.aggregator.SpaceAwareAggregator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.spy;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/06/2020 17:54
 *
 * @author d.velichkevich
 */
public class NameSymbolsValidatorTest extends AbstractBaseMockTestCase {

    @Mock
    private AnalyzingContext contextMock;

    private NameSymbolsValidator validator;

    private InOrder order;


    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        validator = spy(new NameSymbolsValidator());

        order = inOrder(validator, contextMock);
    }

    @Test
    public void testGettingTargetSymbol() throws Exception {
        // Given - When
        char actual = validator.getTargetSymbol();

        // Then
        assertThat(actual).isEqualTo('\u0000');
    }

    @Test
    public void testIsValid() throws Exception {
        // Given
        doReturn(',').when(contextMock).getSymbol();
        doReturn(false).when(validator).isValidSymbol(',');

        // When
        boolean actual = validator.isValid(contextMock);

        // Then
        assertThat(actual).isEqualTo(false);

        order.verify(contextMock).getSymbol();
        order.verify(validator).isValidSymbol(',');
        order.verifyNoMoreInteractions();
    }

    @Test
    public void testIsValidWhenSymbolValidAndIndexZero() throws Exception {
        // Given
        mockTillGetIndexCall('T', 0);

        // When
        boolean actual = validator.isValid(contextMock);

        // Then
        assertThat(actual).isEqualTo(true);

        verifyTillIndexCall('T');
        order.verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @CsvSource(value = {
        "5:test(test),test:true",
        "11:test(test),test:true",
        "1:\"test,test\":true"
    }, delimiter = ':')
    public void testIsValidWhenSymbolValidAndIndexNotZeroAgainstPreviousSpecialSymbol(int index, String text, boolean expectedResult) throws Exception {
        // Given
        mockTillTextCall('t', index, text);

        // When
        boolean actual = validator.isValid(contextMock);

        // Then
        assertThat(actual).isEqualTo(expectedResult);

        verifyTillTextCall('t');
        order.verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/isValidWhenSymbolValidAndIndexNotZeroAgainstPreviousNameSymbolTest.csv", numLinesToSkip = 1)
    public void isValidWhenSymbolValidAndIndexNotZeroAgainstPreviousNameSymbol(
            char symbol, int index, String text, boolean validSymbol,
            char previousSymbol, boolean expectedResult, char expectedSymbol, char expectedPreviousSymbol) throws Exception {

        // Given
        mockTillTextCall(symbol, index, text);
        doReturn(validSymbol).when(validator).isValidSymbol(previousSymbol);

        // When
        boolean actual = validator.isValid(contextMock);

        // Then
        assertThat(actual).isEqualTo(expectedResult);

        verifyTillTextCall(expectedSymbol);
        order.verify(validator).isValidSymbol(expectedPreviousSymbol);
        order.verifyNoMoreInteractions();
    }

    private void mockTillTextCall(char symbol, int index, String text) {
        mockTillGetIndexCall(symbol, index);
        doReturn(text).when(contextMock).getText();
    }

    private void mockTillGetIndexCall(char symbol, int index) {
        doReturn(symbol).when(contextMock).getSymbol();
        doReturn(true).when(validator).isValidSymbol(symbol);
        doReturn(index).when(contextMock).getIndex();
    }

    private void verifyTillTextCall(char expectedSymbol) {
        verifyTillIndexCall(expectedSymbol);
        order.verify(contextMock).getText();
    }

    private void verifyTillIndexCall(char expectedSymbol) {
        order.verify(contextMock).getSymbol();
        order.verify(validator).isValidSymbol(expectedSymbol);
        order.verify(contextMock).getIndex();
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/validNameSymbolTest.csv", delimiter = ',')
    public void testIsValidSymbol(
            @AggregateWith(SpaceAwareAggregator.class) char symbol, @AggregateWith(BooleanValueAggregator.class) boolean expectedValidness) throws Exception {

        // Given - When
        boolean actual = validator.isValidSymbol(symbol);

        // Then
        assertThat(actual).isEqualTo(expectedValidness);
    }
}
