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
public class BracketClosingValidator extends AbstractBaseNameSymbolsAwareValidator {

    private static final int COUNT_LAST_BRACKET = 1;


    public BracketClosingValidator(SymbolValidator nameSymbolValidator) {
        super(nameSymbolValidator);
    }

    @Override
    public char getTargetSymbol() {
        return BRACKET_CLOSING;
    }

    @Override
    public boolean isValid(AnalyzingContext context) {
        int index = context.getIndex();
        int bracketsCount = context.getBracketsCount();
        if ((0 == bracketsCount) || (0 == index)) {
            return false;
        }

        String text = context.getText();
        if ((COUNT_LAST_BRACKET == bracketsCount) && (index < context.getLastIndex()) && (BRACKET_OPENING == text.charAt(0))) {
            return false;
        }

        char previousSymbol = text.charAt(index - 1);
        return (BRACKET_CLOSING == previousSymbol) || getNameSymbolsValidator().isValid(createNewContext(context, previousSymbol));
    }
}
