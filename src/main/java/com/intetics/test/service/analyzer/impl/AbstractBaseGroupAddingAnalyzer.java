package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.StringGroupOrganizingStrategy;
import com.intetics.test.service.analyzer.AbstractBaseSymbolAnalyzer;
import com.intetics.test.service.analyzer.SymbolAnalyzer;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/06/2020 4:08
 *
 * @author d.velichkevich
 */
public abstract class AbstractBaseGroupAddingAnalyzer extends AbstractBaseSymbolAnalyzer {

    private StringGroupOrganizingStrategy strategy;


    public AbstractBaseGroupAddingAnalyzer(SymbolAnalyzer next, StringGroupOrganizingStrategy strategy) {
        super(next);

        this.strategy = strategy;
    }

    @Override
    public AnalyzingErrorResult analyze(AnalyzingContext context) {
        if (getTargetSymbol() != context.getSymbol()) {
            return analyzeNext(context);
        }

        addNewGroup(context);
        context.setTokenName(new StringBuilder());

        return (context.getIndex() < context.getLastIndex()) ? (null) : (analyzeNext(context));
    }

    /**
     * Returns the symbol for which this analyzer is implemented
     *
     * @return {@link Character} (primitive) value
     */
    protected abstract char getTargetSymbol();

    /**
     * Performs operations, associated with the current analyzer implementation, which should create a new string group
     *
     * @param context {@link AnalyzingContext} instance of the current context
     */
    protected abstract void addNewGroup(AnalyzingContext context);


    // Getters And Setters

    public StringGroupOrganizingStrategy getStrategy() {
        return strategy;
    }
}
