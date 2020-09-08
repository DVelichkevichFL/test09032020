package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.StringGroup;
import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.service.StringGroupOrganizingStrategy;
import com.intetics.test.service.analyzer.SymbolAnalyzer;
import com.intetics.test.service.analyzer.validator.SymbolValidator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/06/2020 3:48
 *
 * @author d.velichkevich
 */
public class BracketOpeningAnalyzer extends AbstractBaseGroupAddingAnalyzer {

    public BracketOpeningAnalyzer(SymbolAnalyzer next, StringGroupOrganizingStrategy strategy) {
        super(next, strategy);
    }

    @Override
    protected char getTargetSymbol() {
        return SymbolValidator.BRACKET_OPENING;
    }

    @Override
    protected void addNewGroup(AnalyzingContext context) {
        context.setBracketsCount(context.getBracketsCount() + 1);
        StringGroup group = addCurrentLevelGroup(context, getStrategy());

        if (null != group) {
            context.setCurrentGroup(group);
        }
    }
}
