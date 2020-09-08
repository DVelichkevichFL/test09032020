package com.intetics.test.model.analyzer;

import com.intetics.test.base.AbstractBaseTestCase;
import com.intetics.test.model.StringGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/06/2020 15:19
 *
 * @author d.velichkevich
 */
public class AnalyzingContextTest extends AbstractBaseTestCase {

    private StringGroup group;
    private AnalyzingContext context;


    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        group = new StringGroup();
        context = new AnalyzingContext(9, 25, 'T', "<TEST_STRING>", group);
    }

    @Test
    public void testConstruction() throws Exception {
        // Given - When - Then
        verifyAnalyzingContext(9, 'T', context.getTokenName());
    }

    @Test
    public void testCopyingConstructor() throws Exception {
        // Given - When
        StringBuilder tokenName = context.getTokenName();
        tokenName.append("T");
        context = new AnalyzingContext('N', 0, context);

        // Then
        verifyAnalyzingContext(0, 'N', tokenName);
    }

    private void verifyAnalyzingContext(int expectedIndex, char expectedSymbol, StringBuilder expectedTokenName) {
        assertThat(context.getIndex()).isEqualTo(expectedIndex);
        assertThat(context.getLastIndex()).isEqualTo(25);
        assertThat(context.getSymbol()).isEqualTo(expectedSymbol);
        assertThat(context.getText()).isEqualTo("<TEST_STRING>");
        assertThat(context.getCurrentGroup()).isSameAs(group);

        assertThat(context.getBracketsCount()).isEqualTo(0);
        assertThat(context.getAvailableQuotes()).isSameAs(AnalyzingContext.COUNT_MAXIMUM_QUOTES);
        assertThat(context.getTokenName()).isEqualTo(expectedTokenName);
    }
}