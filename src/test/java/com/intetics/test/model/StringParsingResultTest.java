package com.intetics.test.model;

import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.model.analyzer.AnalyzingStringErrorEnum;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/03/2020 19:54
 *
 * @author d.velichkevich
 */
@RunWith(JUnitPlatform.class)
public class StringParsingResultTest extends Assertions {

    @Test
    public void testInitialization() throws Exception {
        // Given - When
        StringParsingResult result = new StringParsingResult("<TEST_STRING>", new StringGroup());

        // Then
        checkInitialization(result, "<TEST_STRING>", new StringGroup(), false);
    }

    @Test
    public void testInitializationErroneous() throws Exception {
        // Given - When
        StringParsingResult result = new StringParsingResult("<TEST_STRING>", new AnalyzingErrorResult(AnalyzingStringErrorEnum.NOT_ALL_GROUPS_CLOSED));

        // Then
        checkInitialization(result, "<TEST_STRING>", null, true);
    }

    private void checkInitialization(StringParsingResult result, Object expectedPreProcessedString, StringGroup expectedResultGroup, boolean expectedErrorOccurred) {
        assertThat(result.getPreProcessedString()).isEqualTo(expectedPreProcessedString);
        assertThat(result.getParsingResult()).isEqualTo(expectedResultGroup);
        assertThat(result.isErrorOccurred()).isEqualTo(expectedErrorOccurred);
    }
}
