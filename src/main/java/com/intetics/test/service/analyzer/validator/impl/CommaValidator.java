package com.intetics.test.service.analyzer.validator.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.service.analyzer.validator.SymbolValidator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 17:37
 *
 * @author d.velichkevich
 */
public class CommaValidator extends AbstractBaseNameSymbolsAwareValidator {

    public CommaValidator(SymbolValidator nameSymbolValidator) {
        super(nameSymbolValidator);
    }

    @Override
    public char getTargetSymbol() {
        return COMMA;
    }

    @Override
    public boolean isValid(AnalyzingContext context) {
        int index = context.getIndex();
        if (0 == index) {
            return false;
        }

        char previousSymbol = context.getText().charAt(index - 1);
        return (index < context.getLastIndex()) && ((BRACKET_CLOSING == previousSymbol) || getNameSymbolsValidator().isValid(createNewContext(context, previousSymbol)));
    }
}
