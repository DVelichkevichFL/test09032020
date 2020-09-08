package com.intetics.test.service.analyzer;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 17:00
 *
 * @author d.velichkevich
 */
public interface SymbolAnalyzer {

    /**
     * Analyzes the current symbol relative to the current {@code context} to collect all tokens and composite tokens to a structured hierarchy
     *
     * @param context {@link AnalyzingContext} instance of the current context
     * @return {@link AnalyzingErrorResult} instance, if an error occurred or {@code null} otherwise
     */
    AnalyzingErrorResult analyze(AnalyzingContext context);
}
