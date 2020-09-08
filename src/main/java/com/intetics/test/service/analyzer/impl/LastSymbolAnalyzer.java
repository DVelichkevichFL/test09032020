package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.model.analyzer.AnalyzingStringErrorEnum;
import com.intetics.test.service.StringGroupOrganizingStrategy;
import com.intetics.test.service.analyzer.AbstractBaseSymbolAnalyzer;
import com.intetics.test.service.analyzer.SymbolAnalyzer;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 23:31
 *
 * @author d.velichkevich
 */
public class LastSymbolAnalyzer extends AbstractBaseSymbolAnalyzer {

    private StringGroupOrganizingStrategy strategy;


    public LastSymbolAnalyzer(SymbolAnalyzer next, StringGroupOrganizingStrategy strategy) {
        super(next);

        this.strategy = strategy;
    }

    @Override
    public AnalyzingErrorResult analyze(AnalyzingContext context) {
        if (context.getIndex() != context.getLastIndex()) {
            return analyzeNext(context);
        }

        if (context.getBracketsCount() > SIZE_EMPTY) {
            return new AnalyzingErrorResult(AnalyzingStringErrorEnum.NOT_ALL_GROUPS_CLOSED);
        }

        int availableQuotes = context.getAvailableQuotes();
        if ((availableQuotes > SIZE_EMPTY) && (availableQuotes < AnalyzingContext.COUNT_MAXIMUM_QUOTES)) {
            return new AnalyzingErrorResult(AnalyzingStringErrorEnum.FINAL_QUOTATION_MARK_MISSING);
        }

        addCurrentLevelGroup(context, strategy);

        return null;
    }


    // Getters And Setters

    public StringGroupOrganizingStrategy getStrategy() {
        return strategy;
    }
}
