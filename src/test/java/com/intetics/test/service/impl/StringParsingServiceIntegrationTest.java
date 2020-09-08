package com.intetics.test.service.impl;

import com.intetics.test.base.AbstractBaseTestCase;
import com.intetics.test.base.aggregator.EmptyStringAggregator;
import com.intetics.test.model.SortedStringGroup;
import com.intetics.test.model.StringGroup;
import com.intetics.test.model.StringParsingResult;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.StringGroupOrganizingStrategy;
import com.intetics.test.service.StringParsingService;
import com.intetics.test.service.impl.aggregator.ParsingErrorAggregator;
import com.intetics.test.service.impl.aggregator.ParsingStringAggregator;
import com.intetics.test.service.io.ConsoleService;
import com.intetics.test.service.io.impl.ConsoleServiceImpl;
import com.intetics.test.service.io.impl.ResourceServiceImpl;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.provider.CsvFileSource;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/08/2020 1:20
 *
 * @author d.velichkevich
 */
public class StringParsingServiceIntegrationTest extends AbstractBaseTestCase {

    private static final String TAG_SORTED = "sorted";


    private StringParsingService parsingService;

    private ConsoleService consoleService;

    private ByteArrayOutputStream outputStream;


    @BeforeEach
    public void setUp(TestInfo testInfo) throws Exception {
        setUp();

        ResourceServiceImpl resourceService = new ResourceServiceImpl();
        outputStream = new ByteArrayOutputStream();
        consoleService = new ConsoleServiceImpl(resourceService) {

            @Override
            protected PrintStream getConsoleOutputStream() {
                return new PrintStream(outputStream);
            }
        };

        parsingService = new StringParsingServiceImpl((!testInfo.getTags().contains(TAG_SORTED)) ? (null) : (new StringGroupOrganizingStrategy() {

            @Override
            public StringGroup createRoot() {
                return new SortedStringGroup();
            }

            @Override
            public StringGroup create(int level, String token, StringGroup parent) {
                return new SortedStringGroup(level, token, parent);
            }
        }));
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/parsingIntegrationValidStringsTest.csv")
    public void testParsing(String source, @AggregateWith(ParsingStringAggregator.class) String expectedOutput) throws Exception {
        // Given
        StringParsingResult result = parsingService.parse(source);

        // When
        consoleService.generateOutput(result.getParsingResult());

        // When
        assertThat(result.isErrorOccurred()).isFalse();
        assertThat(outputStream.toString()).isEqualTo(expectedOutput);
    }

    @Tag(value = TAG_SORTED)
    @ParameterizedTest
    @CsvFileSource(resources = "/parsingIntegrationValidSortedStringsTest.csv")
    public void testParsingSorted(String source, @AggregateWith(ParsingStringAggregator.class) String expectedOutput) throws Exception {
        // Given
        StringParsingResult result = parsingService.parse(source);

        // When
        consoleService.generateOutput(result.getParsingResult());

        // When
        assertThat(result.isErrorOccurred()).isFalse();
        assertThat(outputStream.toString()).isEqualTo(expectedOutput);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/parsingIntegrationErroneousTest.csv")
    public void testParsingErroneous(
            @AggregateWith(EmptyStringAggregator.class) String source, @AggregateWith(ParsingErrorAggregator.class) AnalyzingErrorResult expectedError) throws Exception {

        // Given - When
        StringParsingResult result = parsingService.parse(source);

        // When
        assertThat(result.isErrorOccurred()).isTrue();
        assertThat(result.getErrorResult()).isEqualTo(expectedError);
    }
}
