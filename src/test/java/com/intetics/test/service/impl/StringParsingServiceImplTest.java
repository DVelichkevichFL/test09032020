package com.intetics.test.service.impl;

import com.intetics.test.base.AbstractBaseMockTestCase;
import com.intetics.test.model.StringGroupMock;
import com.intetics.test.model.StringParsingResult;
import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.model.analyzer.AnalyzingStringErrorEnum;
import com.intetics.test.service.StringGroupOrganizingStrategy;
import com.intetics.test.service.analyzer.AbstractBaseSymbolAnalyzer;
import com.intetics.test.service.analyzer.SymbolAnalyzer;
import com.intetics.test.service.analyzer.impl.BracketClosingAnalyzer;
import com.intetics.test.service.analyzer.impl.BracketOpeningAnalyzer;
import com.intetics.test.service.analyzer.impl.CommaAnalyzer;
import com.intetics.test.service.analyzer.impl.DefaultAnalyzer;
import com.intetics.test.service.analyzer.impl.LastSymbolAnalyzer;
import com.intetics.test.service.analyzer.impl.QuoteAnalyzer;
import com.intetics.test.service.analyzer.impl.SymbolValidityAnalyzer;
import com.intetics.test.service.analyzer.validator.SymbolValidator;
import com.intetics.test.service.analyzer.validator.impl.BracketClosingValidator;
import com.intetics.test.service.analyzer.validator.impl.BracketOpeningValidator;
import com.intetics.test.service.analyzer.validator.impl.CommaValidator;
import com.intetics.test.service.analyzer.validator.impl.NameSymbolsValidator;
import com.intetics.test.service.analyzer.validator.impl.QuoteValidator;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/07/2020 17:55
 *
 * @author d.velichkevich
 */
public class StringParsingServiceImplTest extends AbstractBaseMockTestCase {

    @Mock
    private AnalyzingContext contextMock;

    @Mock
    private StringGroupOrganizingStrategy strategyMock;

    @Mock
    private SymbolAnalyzer chainMock;

    private StringParsingServiceImpl parser;

    private InOrder order;


    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        parser = spy(new StringParsingServiceImpl());

        order = inOrder(parser, contextMock, strategyMock, chainMock);
    }

    @Test
    public void testConstructionOnlyStrategy() throws Exception {
        // Given - When
        parser = new StringParsingServiceImpl(strategyMock) {

            @Override
            void setOrganizingStrategy(StringGroupOrganizingStrategy organizingStrategy) {
                addActions("StringParsingServiceImpl -> setOrganizingStrategy", organizingStrategy);
            }

            @Override
            void setAnalyzersChain(SymbolAnalyzer analyzersChain) {
                addActions("StringParsingServiceImpl -> setAnalyzersChain", analyzersChain);
            }
        };

        // Then
        verifyConstruction(null);
    }

    @Test
    public void testConstructionStrategyAndAnalyzers() throws Exception {
        // Given - When
        parser = new StringParsingServiceImpl(strategyMock, chainMock) {

            @Override
            void setOrganizingStrategy(StringGroupOrganizingStrategy organizingStrategy) {
                addActions("StringParsingServiceImpl -> setOrganizingStrategy", organizingStrategy);
            }

            @Override
            void setAnalyzersChain(SymbolAnalyzer analyzersChain) {
                addActions("StringParsingServiceImpl -> setAnalyzersChain", analyzersChain);
            }
        };

        // Then
        verifyConstruction(chainMock);
    }

    private void verifyConstruction(Object expectedAnalyzersChain) {
        assertThat(parser.getOrganizingStrategy()).isNull();
        assertThat(parser.getAnalyzersChain()).isNull();

        checkActions(
                "StringParsingServiceImpl -> setOrganizingStrategy", strategyMock,
                "StringParsingServiceImpl -> setAnalyzersChain", expectedAnalyzersChain);
    }

    @Test
    public void testSettingOrganizingStrategy() throws Exception {
        // Given - When
        parser.setOrganizingStrategy(strategyMock);

        // Then
        order.verify(parser).setOrganizingStrategy(strategyMock);
        order.verifyNoMoreInteractions();

        assertThat(parser.getOrganizingStrategy()).isSameAs(strategyMock);
    }

    @Test
    public void testSettingOrganizingStrategyNull() throws Exception {
        // Given - When
        parser.setOrganizingStrategy(null);

        // Then
        order.verify(parser).setOrganizingStrategy(null);
        order.verifyNoMoreInteractions();

        assertThat(parser.getOrganizingStrategy()).isInstanceOf(DefaultStringGroupOrganizingStrategy.class);
    }

    @Test
    public void testSettingAnalyzersChain() throws Exception {
        // Given - When
        parser.setAnalyzersChain(chainMock);

        // Then
        order.verify(parser).setAnalyzersChain(chainMock);
        order.verifyNoMoreInteractions();

        assertThat(parser.getAnalyzersChain()).isSameAs(chainMock);
    }

    @Test
    public void testSettingAnalyzersChainNull() throws Exception {
        // Given
        doReturn(chainMock).when(parser).createAnalyzersChain();

        // When
        parser.setAnalyzersChain(null);

        // Then
        order.verify(parser).createAnalyzersChain();
        order.verifyNoMoreInteractions();

        assertThat(parser.getAnalyzersChain()).isSameAs(chainMock);
    }

    @Test
    public void testCreatingAnalyzersChain() throws Exception {
        // Given
        parser = new StringParsingServiceImpl();
        parser.setOrganizingStrategy(strategyMock);
        parser = spy(parser);

        SymbolValidator validatorMock = mock(SymbolValidator.class);
        List<SymbolValidator> validatorsMock = Collections.singletonList(validatorMock);
        doReturn(validatorsMock).when(parser).createValidators();
        doReturn(SymbolValidator.COMMA).when(validatorMock).getTargetSymbol();

        // When
        AbstractBaseSymbolAnalyzer actual = (AbstractBaseSymbolAnalyzer) parser.createAnalyzersChain();

        // Then
        order = inOrder(parser, contextMock, strategyMock, chainMock, validatorMock);
        order.verify(parser).createValidators();
        order.verify(validatorMock).getTargetSymbol();
        order.verifyNoMoreInteractions();

        SymbolAnalyzer nextAnalyzer = verifyChainedAnalyzer(actual, SymbolValidityAnalyzer.class, (AbstractBaseSymbolAnalyzer analyzer)
                -> assertThat(((SymbolValidityAnalyzer) analyzer).getValidatorsMap().get(SymbolValidator.COMMA)).isSameAs(validatorMock));
        nextAnalyzer = verifyChainedAnalyzer(nextAnalyzer, CommaAnalyzer.class, (AbstractBaseSymbolAnalyzer analyzer)
                -> assertThat(((CommaAnalyzer) analyzer).getStrategy()).isSameAs(strategyMock));
        nextAnalyzer = verifyChainedAnalyzer(nextAnalyzer, BracketOpeningAnalyzer.class, (AbstractBaseSymbolAnalyzer analyzer)
                -> assertThat(((BracketOpeningAnalyzer) analyzer).getStrategy()).isSameAs(strategyMock));
        nextAnalyzer = verifyChainedAnalyzer(nextAnalyzer, BracketClosingAnalyzer.class, (AbstractBaseSymbolAnalyzer analyzer)
                -> assertThat(((BracketClosingAnalyzer) analyzer).getStrategy()).isSameAs(strategyMock));
        nextAnalyzer = verifyChainedAnalyzer(nextAnalyzer, QuoteAnalyzer.class, (AbstractBaseSymbolAnalyzer analyzer) -> {});
        nextAnalyzer = verifyChainedAnalyzer(nextAnalyzer, DefaultAnalyzer.class, (AbstractBaseSymbolAnalyzer analyzer) -> {});
        nextAnalyzer = verifyChainedAnalyzer(nextAnalyzer, LastSymbolAnalyzer.class, (AbstractBaseSymbolAnalyzer analyzer)
                -> assertThat(((LastSymbolAnalyzer) analyzer).getStrategy()).isSameAs(strategyMock));
        assertThat(nextAnalyzer).isNull();
    }

    private SymbolAnalyzer verifyChainedAnalyzer(
            SymbolAnalyzer analyzer, Class<? extends AbstractBaseSymbolAnalyzer> expectedClass, Consumer<AbstractBaseSymbolAnalyzer> parametersVerifier) {

        assertThat(analyzer).isInstanceOf(expectedClass);
        parametersVerifier.accept((AbstractBaseSymbolAnalyzer) analyzer);
        return ((AbstractBaseSymbolAnalyzer) analyzer).getNext();
    }

    @Test
    public void testCreatingValidators() throws Exception {
        // Given - When
        List<SymbolValidator> actual = parser.createValidators();

        // Then
        order.verify(parser).createValidators();
        order.verifyNoMoreInteractions();

        final SymbolValidator expectedValidator = actual.iterator().next();
        Iterator<SymbolValidator> actualIterator = actual.iterator();
        Iterator<Class<? extends SymbolValidator>> expectedIterator = Arrays.asList(
                NameSymbolsValidator.class, CommaValidator.class, BracketOpeningValidator.class, BracketClosingValidator.class, QuoteValidator.class).iterator();
        verifyValidator(actualIterator.next(), expectedIterator.next(), (SymbolValidator target) -> {});
        verifyValidator(actualIterator.next(), expectedIterator.next(), (SymbolValidator target)
                -> assertThat(((CommaValidator) target).getNameSymbolsValidator()).isSameAs(expectedValidator));
        verifyValidator(actualIterator.next(), expectedIterator.next(), (SymbolValidator target)
                -> assertThat(((BracketOpeningValidator) target).getNameSymbolsValidator()).isSameAs(expectedValidator));
        verifyValidator(actualIterator.next(), expectedIterator.next(), (SymbolValidator target)
                -> assertThat(((BracketClosingValidator) target).getNameSymbolsValidator()).isSameAs(expectedValidator));
        verifyValidator(actualIterator.next(), expectedIterator.next(), (SymbolValidator target)
                -> assertThat(((QuoteValidator) target).getNameSymbolsValidator()).isSameAs(expectedValidator));
    }

    private void verifyValidator(SymbolValidator validator, Class<? extends SymbolValidator> expectedClass, Consumer<SymbolValidator> parametersValidator) {
        assertThat(validator).isInstanceOf(expectedClass);
        parametersValidator.accept(validator);
    }

    @Test
    public void testUpdatingContext() throws Exception {
        // Given
        doNothing().when(contextMock).setIndex(2);
        doNothing().when(contextMock).setSymbol('s');

        // When
        parser.updateContext(contextMock, "test", 2);

        // Then
        order.verify(contextMock).setIndex(2);
        order.verify(contextMock).setSymbol('s');
        order.verifyNoMoreInteractions();
    }

    @ParameterizedTest
    @CsvSource(value = {
        "(te  st);2;7",
        "\"    (     test   (   test    ,  test   (    te     st    )    )    ,   test   )    \";46;54",
        "\"    (     test   (   test    ,   test,te   st    ,  test   (    test    )    )    ,   test   )    \";40;46",
        "\"    (     test   (   test    ,   test,te st    ,  test   (    test    )    )    ,   test   )    \";40;44",
        "\"    (     test   (   test    ,   test,te\tst    ,  test   (    test    )    )    ,   test   )    \";40;44",
        "\"    (     test   (   test    ,   test,te\t\t\tst    ,  test   (    test    )    )    ,   test   )    \";40;46",
        "\"    (     test   (   test    ,   test,te \t \t \t st    ,  test   (    test    )    )    ,   test   )    \";40;50",
        "\"    (     test   (   test    ,   test,te   st    ,  te   st   (    t est    )    )    ,   test   )    \";40;46"
    }, delimiter = ';')
    public void testParsingInvalidTokenName(String source, int expectedStartIndex, int expectedEndIndex) throws Exception {
        // When
        StringParsingResult actual = parser.parse(source);

        // Then
        order.verify(parser).parse(source);
        order.verifyNoMoreInteractions();

        assertThat(actual).isEqualTo(new StringParsingResult(source, new AnalyzingErrorResult(expectedStartIndex, expectedEndIndex)));
    }

    @Test
    public void testParsing() throws Exception {
        // Given - When - Then
        AnalyzingContext context = new AnalyzingContext(0, 3, '\u0000', "test", new StringGroupMock());
        checkParsing(4, context, null, new StringParsingResult("test", new StringGroupMock()));
    }

    @Test
    public void testParsingWithError() throws Exception {
        // Given - When - Then
        AnalyzingContext context = new AnalyzingContext(0, 3, '\u0000', "test", new StringGroupMock());
        AnalyzingErrorResult errorResult = new AnalyzingErrorResult(AnalyzingStringErrorEnum.NOT_ALL_GROUPS_CLOSED);
        StringParsingResult expectedResult = new StringParsingResult("test", errorResult);
        checkParsing(1, context, errorResult, expectedResult);
    }

    private void checkParsing(int length, AnalyzingContext context, AnalyzingErrorResult errorResult, StringParsingResult expectedResult) throws Exception{
        // Given
        parser.setOrganizingStrategy(strategyMock);
        parser.setAnalyzersChain(chainMock);

        StringGroupMock root = new StringGroupMock();
        doReturn(root).when(strategyMock).createRoot();
        for (int i = 0; i < length; i++) {
            doNothing().when(parser).updateContext(context, "test", i);
            when(chainMock.analyze(context)).thenReturn(errorResult);
        }

        // When
        StringParsingResult actual = parser.parse("test");

        // Then
        assertThat(actual).isEqualTo(expectedResult);

        order.verify(strategyMock).createRoot();
        for (int i = 0; i < length; i++) {
            order.verify(parser).updateContext(context, "test", i);
            order.verify(chainMock).analyze(context);
        }
        order.verifyNoMoreInteractions();
    }

}
