package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.analyzer.AbstractBaseSymbolAnalyzerTestCase;
import com.intetics.test.service.analyzer.impl.aggregator.ZeroSymbolKeyAggregator;
import com.intetics.test.service.analyzer.validator.SymbolValidator;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InOrder;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/07/2020 16:36
 *
 * @author d.velichkevich
 */
public class SymbolValidityAnalyzerTest extends AbstractBaseSymbolAnalyzerTestCase<SymbolValidityAnalyzer> {

    private Map<Character, SymbolValidator> validatorsMap;


    @BeforeEach
    @Override
    public void setUp() throws Exception {
        validatorsMap = new HashMap<>();
        validatorsMap.put(SymbolValidityAnalyzer.KEY_DEFAULT_VALIDATOR, mock(SymbolValidator.class));
        validatorsMap.put(SymbolValidator.QUOTE, mock(SymbolValidator.class));
        validatorsMap.put(SymbolValidator.COMMA, mock(SymbolValidator.class));

        super.setUp();

        setOrder(inOrder(
                getAnalyzer(), getNextMock(), getContextMock(), getStrategyMock(),
                validatorsMap.get(SymbolValidityAnalyzer.KEY_DEFAULT_VALIDATOR), validatorsMap.get(SymbolValidator.QUOTE), validatorsMap.get(SymbolValidator.COMMA)));
    }

    @Override
    protected SymbolValidityAnalyzer createAnalyzer() {
        SymbolValidityAnalyzer result = new SymbolValidityAnalyzer(getNextMock());
        result.setValidatorsMap(validatorsMap);
        return result;
    }

    @Override
    protected SymbolValidityAnalyzer createAnalyzerWithNullNext() {
        SymbolValidityAnalyzer result = new SymbolValidityAnalyzer(null);
        result.setValidatorsMap(validatorsMap);
        return result;
    }

    @Test
    public void testConstructionWithValidators() throws Exception {
        // Given - When
        List<SymbolValidator> validators = Arrays.asList(mock(SymbolValidator.class), mock(SymbolValidator.class));
        new SymbolValidityAnalyzer(getNextMock(), validators) {

            @Override
            void initialize(List<SymbolValidator> validators) {
                addActions("SymbolValidityAnalyzer -> initialize", validators);
            }
        };

        // Then
        checkActions("SymbolValidityAnalyzer -> initialize", validators);
    }

    @Test
    public void testInitializing() throws Exception {
        // Given
        SymbolValidator firstValidator = mock(SymbolValidator.class);
        when(firstValidator.getTargetSymbol()).thenReturn(SymbolValidityAnalyzer.KEY_DEFAULT_VALIDATOR);

        SymbolValidator secondValidator = mock(SymbolValidator.class);
        when(secondValidator.getTargetSymbol()).thenReturn(SymbolValidator.COMMA);

        getAnalyzer().setValidatorsMap(new HashMap<>());

        // When
        getAnalyzer().initialize(Arrays.asList(firstValidator, secondValidator));

        // Then
        Map<Character, SymbolValidator> expectedValidatorsMap = new HashMap<>();
        expectedValidatorsMap.put(SymbolValidityAnalyzer.KEY_DEFAULT_VALIDATOR, firstValidator);
        expectedValidatorsMap.put(SymbolValidator.COMMA, secondValidator);
        assertThat(getAnalyzer().getValidatorsMap()).isEqualTo(expectedValidatorsMap);

        InOrder order = inOrder(getAnalyzer(), getNextMock(), getContextMock(), getStrategyMock(), firstValidator, secondValidator);
        order.verify(firstValidator).getTargetSymbol();
        order.verify(secondValidator).getTargetSymbol();
        order.verify(getAnalyzer()).getValidatorsMap();
        order.verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @CsvSource(value = {
        SymbolValidator.QUOTE + ";" + SymbolValidator.QUOTE,
        SymbolValidator.COMMA + ";" + SymbolValidator.COMMA,
        "t;" + SymbolValidityAnalyzer.KEY_DEFAULT_VALIDATOR
    }, delimiter = ';')
    public void testAnalyzing(@AggregateWith(ZeroSymbolKeyAggregator.class) char symbol, @AggregateWith(ZeroSymbolKeyAggregator.class) char key) throws Exception {
        // Given
        SymbolValidator validator = mockTillValidationCall(symbol, key, true);
        doReturn(null).when(getAnalyzer()).analyzeNext(getContextMock());

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isNull();

        verifyTillValidatingCall(validator);
        getOrder().verify(getAnalyzer()).analyzeNext(getContextMock());
        getOrder().verifyNoMoreInteractions();
    }

    @Test
    public void testAnalyzingWhenSymbolInvalid() throws Exception {
        // Given
        SymbolValidator validator = mockTillValidationCall(' ', SymbolValidityAnalyzer.KEY_DEFAULT_VALIDATOR, false);
        doReturn(2).when(getContextMock()).getIndex();

        // When
        AnalyzingErrorResult actual = getAnalyzer().analyze(getContextMock());

        // Then
        assertThat(actual).isEqualTo(new AnalyzingErrorResult(' ', 3));

        verifyTillValidatingCall(validator);
        getOrder().verify(getContextMock()).getIndex();
        getOrder().verifyNoMoreInteractions();
    }

    private SymbolValidator mockTillValidationCall(char symbol, char key, boolean validationResult) {
        doReturn(symbol).when(getContextMock()).getSymbol();
        SymbolValidator validator = validatorsMap.get(key);
        doReturn(validationResult).when(validator).isValid(getContextMock());
        return validator;
    }

    private void verifyTillValidatingCall(SymbolValidator validator) {
        getOrder().verify(getContextMock()).getSymbol();
        getOrder().verify(validator).isValid(getContextMock());
    }
}
