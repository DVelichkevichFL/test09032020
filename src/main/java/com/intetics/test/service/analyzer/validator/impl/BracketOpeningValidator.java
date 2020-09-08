package com.intetics.test.service.analyzer.validator.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.service.analyzer.validator.SymbolValidator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 17:35
 *
 * @author d.velichkevich
 */
public class BracketOpeningValidator extends AbstractBaseNameSymbolsAwareValidator {

    public BracketOpeningValidator(SymbolValidator nameSymbolValidator) {
        super(nameSymbolValidator);
    }

    @Override
    public char getTargetSymbol() {
        return BRACKET_OPENING;
    }

    @Override
    public boolean isValid(AnalyzingContext context) {
        int index = context.getIndex();
        if (0 == index) {
            return true;
        }

        if (context.getLastIndex() == index) {
            return false;
        }

        char previousSymbol = context.getText().charAt(index - 1);
        return (QUOTE == previousSymbol) || getNameSymbolsValidator().isValid(createNewContext(context, previousSymbol));
    }
}
