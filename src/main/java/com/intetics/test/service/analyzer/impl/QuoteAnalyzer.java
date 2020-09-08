package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.analyzer.AbstractBaseSymbolAnalyzer;
import com.intetics.test.service.analyzer.SymbolAnalyzer;
import com.intetics.test.service.analyzer.validator.SymbolValidator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/06/2020 1:47
 *
 * @author d.velichkevich
 */
public class QuoteAnalyzer extends AbstractBaseSymbolAnalyzer {

    public QuoteAnalyzer(SymbolAnalyzer next) {
        super(next);
    }

    @Override
    public AnalyzingErrorResult analyze(AnalyzingContext context) {
        if (SymbolValidator.QUOTE != context.getSymbol()) {
            return analyzeNext(context);
        }

        context.setAvailableQuotes(context.getAvailableQuotes() - 1);

        return (context.getIndex() < context.getLastIndex()) ? (null) : (analyzeNext(context));
    }
}
