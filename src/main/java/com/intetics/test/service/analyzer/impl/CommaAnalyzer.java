package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.service.StringGroupOrganizingStrategy;
import com.intetics.test.service.analyzer.SymbolAnalyzer;
import com.intetics.test.service.analyzer.validator.SymbolValidator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/06/2020 0:01
 *
 * @author d.velichkevich
 */
public class CommaAnalyzer extends AbstractBaseGroupAddingAnalyzer {

    public CommaAnalyzer(SymbolAnalyzer next, StringGroupOrganizingStrategy strategy) {
        super(next, strategy);
    }

    @Override
    protected char getTargetSymbol() {
        return SymbolValidator.COMMA;
    }

    @Override
    protected void addNewGroup(AnalyzingContext context) {
        addCurrentLevelGroup(context, getStrategy());
    }
}
