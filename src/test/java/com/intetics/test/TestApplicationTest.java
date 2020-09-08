package com.intetics.test;

import com.intetics.test.base.AbstractBaseMockTestCase;
import com.intetics.test.model.SortedStringGroup;
import com.intetics.test.model.StringGroupMock;
import com.intetics.test.model.StringParsingResult;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.model.analyzer.AnalyzingStringErrorEnum;
import com.intetics.test.service.StringGroupOrganizingStrategy;
import com.intetics.test.service.StringParsingService;
import com.intetics.test.service.impl.DefaultStringGroupOrganizingStrategy;
import com.intetics.test.service.impl.StringGroupOrganizingStrategyMock;
import com.intetics.test.service.io.ConsoleService;
import com.intetics.test.service.io.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InOrder;
import org.mockito.Mock;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.spy;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/03/2020 20:21
 *
 * @author d.velichkevich
 */
public class TestApplicationTest extends AbstractBaseMockTestCase {

    private static final String TEST_ERRONEOUS_PART = "test(test(test)test";


    @Mock
    private ConsoleService consoleServiceMock;

    @Mock
    private StringParsingService parserMock;

    private StringGroupOrganizingStrategy defaultStrategyMock;

    private StringGroupOrganizingStrategy sortedStrategyMock;

    private TestApplication application;

    private InOrder order;


    @BeforeEach
    public void setUp() throws Exception {
        super.setUp();

        application = spy(new TestApplication());
        setMocks();

        defaultStrategyMock = new StringGroupOrganizingStrategyMock();
        sortedStrategyMock = new StringGroupOrganizingStrategyMock();

        order = inOrder(consoleServiceMock, parserMock, application);
    }

    private void setMocks() {
        application.setConsoleService(consoleServiceMock);
        application.setParser(parserMock);
        application.setStrategyName(TEST_DEFAULT_STRATEGY);
    }

    @Test
    public void testInitialization() throws Exception {
        // Given
        ResourceService resourceServiceMock = getResourceServiceMock();
        doReturn(resourceServiceMock).when(application).createResourceService();
        doReturn(consoleServiceMock).when(application).createConsoleService(resourceServiceMock);
        doReturn(TEST_DEFAULT_STRATEGY).when(resourceServiceMock).getProperty(TestApplication.KEY_GROUPS_ORGANIZATION_STRATEGY, TEST_DEFAULT_STRATEGY);
        doReturn(defaultStrategyMock).when(application).getStringGroupOrganizingStrategy(TEST_DEFAULT_STRATEGY);
        doReturn(parserMock).when(application).createStringParsingService(defaultStrategyMock);

        // When
        application.initialize();

        // Then
        InOrder order = inOrder(application, resourceServiceMock);
        order.verify(application).createResourceService();
        order.verify(application).createConsoleService(resourceServiceMock);
        order.verify(resourceServiceMock).getProperty(TestApplication.KEY_GROUPS_ORGANIZATION_STRATEGY, TEST_DEFAULT_STRATEGY);
        order.verify(application).getStringGroupOrganizingStrategy(TEST_DEFAULT_STRATEGY);
        order.verify(application).createStringParsingService(defaultStrategyMock);

        order.verifyNoMoreInteractions();

        assertThat(application.getConsoleService()).isSameAs(consoleServiceMock);
        assertThat(application.getParser()).isSameAs(parserMock);
        assertThat(application.getStrategyName()).isSameAs(TEST_DEFAULT_STRATEGY);
    }

    @Test
    public void testGettingStringGroupOrganizingStrategy() throws Exception {
        // Given - When - Then
        checkGettingStringGroupOrganizationStrategy(TEST_DEFAULT_STRATEGY, defaultStrategyMock);
    }

    @ParameterizedTest
    @EnumSource(value = AnalyzingStringErrorEnum.class, names = {
        "INVALID_TOKEN",
        "UNEXPECTED_SYMBOL_AT_INDEX",
        "NOT_ALL_GROUPS_CLOSED",
        "FINAL_QUOTATION_MARK_MISSING"
    })
    public void testExecutionWhenError(AnalyzingStringErrorEnum error) throws Exception {
        // Given - When - Then
        StringParsingResult parsingResult = new StringParsingResult(TEST_ERRONEOUS_PART, new AnalyzingErrorResult(error));
        checkAlmostFullyExecution(TEST_ERRONEOUS_SOURCE_STRING, parsingResult);

        // Then
        order.verify(consoleServiceMock).println(error.getErrorKey(), error.getMessageParameters(parsingResult));
        order.verifyNoMoreInteractions();
    }

    @Test
    public void testGettingStringGroupOrganizingStrategyForNoneDefaultStrategy() throws Exception {
        // Given - When - Then
        checkGettingStringGroupOrganizationStrategy("sorted", sortedStrategyMock);
    }

    private void checkGettingStringGroupOrganizationStrategy(String strategyName, StringGroupOrganizingStrategy expectedStrategy) {
        // Given
        lenient().doReturn(defaultStrategyMock).when(application).createDefaultStringGroupOrganizingStrategy();
        lenient().doReturn(sortedStrategyMock).when(application).createSortedStringGroupOrganizingStrategy();

        // When
        StringGroupOrganizingStrategy actual = application.getStringGroupOrganizingStrategy(strategyName);

        // Then
        assertThat(actual).isSameAs(expectedStrategy);
    }

    @Test
    public void testCreatingDefaultStringGroupOrganizingStrategy() throws Exception {
        // Given - When
        StringGroupOrganizingStrategy actual = application.createDefaultStringGroupOrganizingStrategy();

        // Then
        assertThat(actual).isInstanceOf(DefaultStringGroupOrganizingStrategy.class);
    }

    @Test
    public void testCreatingSortedStringGroupOrganizingStrategy() throws Exception {
        // Given - When
        StringGroupOrganizingStrategy actual = application.createSortedStringGroupOrganizingStrategy();

        // Then
        assertThat(actual.createRoot()).isEqualTo(new SortedStringGroup());
        assertThat(actual.create(1, "TEST_TOKEN", new SortedStringGroup())).isEqualTo(new SortedStringGroup(1, "TEST_TOKEN", new SortedStringGroup()));
    }

    @Test
    public void testExecution() throws Exception {
        // Given - When - Then
        checkAlmostFullyExecution(TEST_SOURCE_STRING, new StringParsingResult("", new StringGroupMock()));

        // Then
        order.verify(consoleServiceMock).generateOutput(new StringGroupMock());
        order.verifyNoMoreInteractions();
    }

    private void checkAlmostFullyExecution(String sourceString, StringParsingResult parsingResult) {
        // Given
        doReturn(sourceString).when(application).getSourceString(new String[0], TEST_DEFAULT_STRATEGY);
        doReturn(parsingResult).when(parserMock).parse(sourceString);

        // When
        application.execute(new String[0]);

        // Then
        order.verify(application).getSourceString(new String[0], TEST_DEFAULT_STRATEGY);
        order.verify(parserMock).parse(sourceString);
    }

    @Test
    public void testGettingSourceString() throws Exception {
        // Given - When
        String actual = application.getSourceString(new String[] {
            TEST_SOURCE_STRING
        }, TEST_DEFAULT_STRATEGY);

        // Then
        assertThat(actual).isSameAs(TEST_SOURCE_STRING);
    }

    @Test
    public void testGettingSourceStringWhenCommandLineArgumentsEmpty() throws Exception {
        // Given - When - Then
        checkGettingSourceStringForRequestingString(new String[0]);
    }

    @Test
    public void testGettingSourceStringWhenCommandLineArgumentIsEmptyString() throws Exception {
        // Given - When - Then
        checkGettingSourceStringForRequestingString(new String[] {
            ""
        });
    }

    private void checkGettingSourceStringForRequestingString(String[] arguments) {
        // Given
        doReturn(TEST_ERRONEOUS_SOURCE_STRING).when(consoleServiceMock).requestString(TEST_DEFAULT_STRATEGY);

        // When
        String actual = application.getSourceString(arguments, TEST_DEFAULT_STRATEGY);

        // Then
        order.verify(consoleServiceMock).requestString(TEST_DEFAULT_STRATEGY);
        order.verifyNoMoreInteractions();

        assertThat(actual).isSameAs(TEST_ERRONEOUS_SOURCE_STRING);
    }
}
