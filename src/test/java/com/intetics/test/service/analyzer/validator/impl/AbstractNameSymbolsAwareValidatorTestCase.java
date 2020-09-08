package com.intetics.test.service.analyzer.validator.impl;

import com.intetics.test.base.AbstractBaseMockTestCase;
import com.intetics.test.model.StringGroup;
import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.service.analyzer.validator.SymbolValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/06/2020 21:23
 *
 * @author d.velichkevich
 *
 * @param <T> &lt;T extends {@link AbstractBaseNameSymbolsAwareValidator}&gt; type of the validator to test
 */
public abstract class AbstractNameSymbolsAwareValidatorTestCase<T extends AbstractBaseNameSymbolsAwareValidator> extends AbstractBaseMockTestCase {

    @Mock
    private SymbolValidator validatorMock;

    @Mock
    private AnalyzingContext contextMock;

    private T validator;

    private InOrder order;


    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        validator = spy(createValidator());

        order = inOrder(validator, validatorMock, contextMock);
    }

    /**
     * Creates a {@link SymbolValidator} instance for the current test
     *
     * @return {@link SymbolValidator} instance
     */
    protected abstract T createValidator();

    @Test
    public void testConstruction() throws Exception {
        // Given - When
        SymbolValidator actual = validator.getNameSymbolsValidator();

        // Then
        assertThat(actual).isSameAs(validatorMock);
    }

    @Test
    public void testGettingTargetSymbol() throws Exception {
        // Given - When
        char actual = validator.getTargetSymbol();

        // Then
        assertThat(actual).isSameAs(getExpectedTargetSymbol());
    }

    /**
     * Gets the expected target symbol of the validator to test
     *
     * @return {@link Character} (primitive) value of the expected symbol
     */
    protected abstract char getExpectedTargetSymbol();

    @Test
    public void testCreatingNewContext() throws Exception {
        // Given
        doReturn(8).when(contextMock).getLastIndex();
        doReturn("(((test1)").when(contextMock).getText();

        StringGroup groupMock = mock(StringGroup.class);
        doReturn(groupMock).when(contextMock).getCurrentGroup();
        doReturn(2).when(contextMock).getBracketsCount();
        doReturn(AnalyzingContext.COUNT_MAXIMUM_QUOTES).when(contextMock).getAvailableQuotes();
        doReturn(new StringBuilder("test1")).when(contextMock).getTokenName();

        // When
        AnalyzingContext actual = validator.createNewContext(contextMock, 'T');

        // Then
        AnalyzingContext expectedContext = new AnalyzingContext(0, 8, 'T', "(((test1)", groupMock);
        expectedContext.setBracketsCount(2);
        expectedContext.setAvailableQuotes(AnalyzingContext.COUNT_MAXIMUM_QUOTES);
        expectedContext.setTokenName(new StringBuilder("test1"));
        assertThat(actual).isEqualTo(expectedContext);

        order.verify(contextMock).getLastIndex();
        order.verify(contextMock).getText();
        order.verify(contextMock).getCurrentGroup();
        order.verify(contextMock).getBracketsCount();
        order.verify(contextMock).getAvailableQuotes();
        order.verify(contextMock).getTokenName();
        order.verifyNoMoreInteractions();
    }

    /**
     * Mocks all the necessary operations to check that {@code previousSymbol} is {@code previousSymbolValid}
     *
     * @param previousSymbol {@link Character} (primitive) value of the previous symbol
     * @param previousSymbolValid {@link Boolean} (primitive) value which controls the required validity of the symbol
     * @return {@link AnalyzingContext} instance of the new context mock
     */
    protected AnalyzingContext mockPreviousSymbolValidNameSymbol(char previousSymbol, boolean previousSymbolValid) {
        AnalyzingContext result = mock(AnalyzingContext.class);
        doReturn(result).when(validator).createNewContext(contextMock, previousSymbol);
        doReturn(previousSymbolValid).when(validatorMock).isValid(result);
        return result;
    }

    /**
     * Verifies that all operations to check the previous symbol of the name have been called
     *
     * @param expectedPreviousSymbol {@link Character} (primitive) value of the expected previous symbol
     * @param newContextMock {@link AnalyzingContext} new context instance
     */
    protected void verifyPreviousSymbolValidNameSymbol(char expectedPreviousSymbol, AnalyzingContext newContextMock) {
        order.verify(validator).createNewContext(contextMock, expectedPreviousSymbol);
        order.verify(validatorMock).isValid(newContextMock);
        order.verifyNoMoreInteractions();
    }


    // Getters And Setters

    protected SymbolValidator getValidatorMock() {
        return validatorMock;
    }

    protected AnalyzingContext getContextMock() {
        return contextMock;
    }

    protected T getValidator() {
        return validator;
    }

    protected InOrder getOrder() {
        return order;
    }
}
