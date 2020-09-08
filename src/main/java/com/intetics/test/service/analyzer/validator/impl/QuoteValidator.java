package com.intetics.test.service.analyzer.validator.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.service.analyzer.validator.SymbolValidator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 17:36
 *
 * @author d.velichkevich
 */
public class QuoteValidator extends AbstractBaseNameSymbolsAwareValidator {

    public QuoteValidator(SymbolValidator nameSymbolsValidator) {
        super(nameSymbolsValidator);
    }

    @Override
    public char getTargetSymbol() {
        return QUOTE;
    }

    @Override
    public boolean isValid(AnalyzingContext context) {
        int index = context.getIndex();
        if (0 == index) {
            return true;
        }

        if ((AnalyzingContext.COUNT_MAXIMUM_QUOTES == context.getAvailableQuotes()) && (index > 0)) {
            return false;
        }

        char previousSymbol = context.getText().charAt(index - 1);
        return (context.getLastIndex() == index) && ((BRACKET_CLOSING == previousSymbol) || getNameSymbolsValidator().isValid(createNewContext(context, previousSymbol)));
    }
}
