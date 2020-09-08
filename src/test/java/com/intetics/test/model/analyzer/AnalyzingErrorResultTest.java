package com.intetics.test.model.analyzer;

import com.intetics.test.base.AbstractBaseTestCase;
import org.junit.jupiter.api.Test;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/06/2020 15:08
 *
 * @author d.velichkevich
 */
public class AnalyzingErrorResultTest extends AbstractBaseTestCase {

    @Test
    public void testConstruction() throws Exception {
        // Given - When
        AnalyzingErrorResult result = new AnalyzingErrorResult('T', 14);

        // Then
        assertThat(result.getErrorType()).isSameAs(AnalyzingStringErrorEnum.UNEXPECTED_SYMBOL_AT_INDEX);
        assertThat(result.getUnexpectedSymbol()).isEqualTo('T');
        assertThat(result.getUnexpectedSymbolIndex()).isEqualTo(14);
        assertThat(result.getInvalidTokenEndIndex()).isEqualTo(0);
    }

    @Test
    public void testConstructionForInvalidToken() throws Exception {
        // Given - When
        AnalyzingErrorResult result = new AnalyzingErrorResult(11, 17);

        // Then
        assertThat(result.getErrorType()).isSameAs(AnalyzingStringErrorEnum.INVALID_TOKEN);
        assertThat(result.getUnexpectedSymbol()).isEqualTo('\u0000');
        assertThat(result.getUnexpectedSymbolIndex()).isEqualTo(11);
        assertThat(result.getInvalidTokenEndIndex()).isEqualTo(17);
    }
}
