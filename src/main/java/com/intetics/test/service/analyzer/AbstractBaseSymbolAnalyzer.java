package com.intetics.test.service.analyzer;

import com.intetics.test.model.StringGroup;
import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.StringGroupOrganizingStrategy;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 21:47
 *
 * @author d.velichkevich
 */
public abstract class AbstractBaseSymbolAnalyzer implements SymbolAnalyzer {

    protected static final int SIZE_EMPTY = 0;


    private SymbolAnalyzer next;


    public AbstractBaseSymbolAnalyzer(SymbolAnalyzer next) {
        this.next = next;
    }

    /**
     * Delegates analyzing to the next analyzer, if it is set
     *
     * @param context {@link AnalyzingContext} instance of the current context
     * @return {@link AnalyzingErrorResult} instance
     */
    public AnalyzingErrorResult analyzeNext(AnalyzingContext context) {
        return (null == next) ? (null) : (next.analyze(context));
    }

    /**
     * Creates and adds a new group of the current level. The parent is the current group in context
     *
     * @param context {@link AnalyzingContext} instance of the current context
     * @param strategy {@link StringGroupOrganizingStrategy} instance of the current group organizing strategy
     * @return {@link StringGroup} instance of the newly created group, or {@code null}, if the token name is empty
     */
    public StringGroup addCurrentLevelGroup(AnalyzingContext context, StringGroupOrganizingStrategy strategy) {
        StringBuilder tokenName = context.getTokenName();
        if (isTokenNameEmpty(tokenName)) {
            return null;
        }

        StringGroup currentGroup = context.getCurrentGroup();
        StringGroup result = strategy.create((currentGroup.getGroupLevel() + 1), tokenName.toString(), currentGroup);
        currentGroup.getChildrenGroups().add(result);
        return result;
    }

    /**
     * Checks if the specified {@code tokenName} is empty
     *
     * @param tokenName {@link StringBuilder} instance of the current token name
     * @return {@link Boolean} (primitive) value: {@code true} if {@code tokenName} is empty, and {@code false} otherwise
     */
    public boolean isTokenNameEmpty(StringBuilder tokenName) {
        return SIZE_EMPTY == tokenName.length();
    }


    // Getters And Setters

    public SymbolAnalyzer getNext() {
        return next;
    }
}
