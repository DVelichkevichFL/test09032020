package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.analyzer.AbstractBaseSymbolAnalyzer;
import com.intetics.test.service.analyzer.SymbolAnalyzer;
import com.intetics.test.service.analyzer.validator.SymbolValidator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 23:58
 *
 * @author d.velichkevich
 *
 * This {@link SymbolAnalyzer} should be executed after all other analyzers have not accepted the current symbol
 */
public class DefaultAnalyzer extends AbstractBaseSymbolAnalyzer {

    public DefaultAnalyzer(SymbolAnalyzer next) {
        super(next);
    }

    @Override
    public AnalyzingErrorResult analyze(AnalyzingContext context) {
        char symbol = context.getSymbol();
        if (!SymbolValidator.SYMBOLS_ALL_SPECIALS.contains(symbol)) {
            context.getTokenName().append(symbol);
        }
        return analyzeNext(context);
    }
}
