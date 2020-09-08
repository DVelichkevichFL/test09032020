package com.intetics.test.service.analyzer;

import com.intetics.test.base.AbstractBaseMockTestCase;
import com.intetics.test.model.StringGroup;
import com.intetics.test.model.StringGroupMock;
import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.StringGroupOrganizingStrategy;
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
 * Created : 09/07/2020 5:27
 *
 * @author d.velichkevich
 *
 * @param <T> &lt;T extends {@link AbstractBaseSymbolAnalyzer}&gt; type of the analyzer to test
 */
public abstract class AbstractBaseSymbolAnalyzerTestCase<T extends AbstractBaseSymbolAnalyzer> extends AbstractBaseMockTestCase {

    @Mock
    private SymbolAnalyzer nextMock;

    @Mock
    private AnalyzingContext contextMock;

    @Mock
    private StringGroupOrganizingStrategy strategyMock;

    private T analyzer;

    private InOrder order;


    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();

        analyzer = spy(createAnalyzer());

        order = inOrder(analyzer, nextMock, contextMock, strategyMock);
    }

    /**
     * Creates analyzer instance to test
     *
     * @return {@link com.intetics.test.service.analyzer.SymbolAnalyzer} instance
     */
    protected abstract T createAnalyzer();

    /**
     * Creates analyzer instance to test
     *
     * @return {@link com.intetics.test.service.analyzer.SymbolAnalyzer} instance
     */
    protected abstract T createAnalyzerWithNullNext();

    @Test
    public void testConstruction() throws Exception {
        // Given - When
        SymbolAnalyzer actual = analyzer.getNext();

        // Then
        assertThat(actual).isSameAs(nextMock);
    }

    @Test
    public void testAnalyzingNext() throws Exception {
        // Given
        AnalyzingErrorResult errorResultMock = mock(AnalyzingErrorResult.class);
        doReturn(errorResultMock).when(nextMock).analyze(contextMock);

        // When
        AnalyzingErrorResult actual = analyzer.analyzeNext(contextMock);

        // Then
        assertThat(actual).isSameAs(errorResultMock);

        order.verify(nextMock).analyze(contextMock);
        order.verifyNoMoreInteractions();
    }

    @Test
    public void testAnalyzingNextWhenNextNull() throws Exception {
        // Given
        analyzer = spy(createAnalyzerWithNullNext());

        // When
        AnalyzingErrorResult actual = analyzer.analyzeNext(contextMock);

        // Then
        assertThat(actual).isNull();

        order.verifyNoMoreInteractions();
    }

    @Test
    public void testAddingCurrentLevelGroup() throws Exception {
        // Given
        StringBuilder tokenName = new StringBuilder("test");
        doReturn(tokenName).when(contextMock).getTokenName();
        doReturn(false).when(analyzer).isTokenNameEmpty(tokenName);

        StringGroupMock groupMock = new StringGroupMock();
        doReturn(groupMock).when(contextMock).getCurrentGroup();
        StringGroupMock newGroupMock = new StringGroupMock(0, "test", groupMock);
        doReturn(newGroupMock).when(strategyMock).create(0, "test", groupMock);

        // When
        StringGroup actual = analyzer.addCurrentLevelGroup(contextMock, strategyMock);

        // Then
        StringGroupMock expectedGroup = new StringGroupMock(0, "test", new StringGroupMock());
        assertThat(actual).isEqualTo(expectedGroup);
        assertThat(actual.getParentGroup()).isSameAs(groupMock);
    }

    @Test
    public void testAddingCurrentLevelGroupForEmptyTokenName() throws Exception {
        // Given
        StringBuilder tokenName = new StringBuilder();
        doReturn(tokenName).when(contextMock).getTokenName();
        doReturn(true).when(analyzer).isTokenNameEmpty(tokenName);

        // When
        StringGroup actual = analyzer.addCurrentLevelGroup(contextMock, strategyMock);

        // Then
        assertThat(actual).isNull();
    }


    // Getters And Setters

    protected SymbolAnalyzer getNextMock() {
        return nextMock;
    }

    protected AnalyzingContext getContextMock() {
        return contextMock;
    }

    protected StringGroupOrganizingStrategy getStrategyMock() {
        return strategyMock;
    }

    protected T getAnalyzer() {
        return analyzer;
    }

    protected InOrder getOrder() {
        return order;
    }

    protected void setOrder(InOrder order) {
        this.order = order;
    }
}
