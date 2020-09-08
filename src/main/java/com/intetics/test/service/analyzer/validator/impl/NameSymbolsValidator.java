package com.intetics.test.service.analyzer.validator.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.service.analyzer.validator.SymbolValidator;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 17:38
 *
 * @author d.velichkevich
 */
public class NameSymbolsValidator implements SymbolValidator {

    private static final Set<Character> SYMBOLS_VALID = new HashSet<>(Arrays.asList(QUOTE, COMMA, BRACKET_OPENING));


    @Override
    public char getTargetSymbol() {
        return 0;
    }

    @Override
    public boolean isValid(AnalyzingContext context) {
        char symbol = context.getSymbol();
        if (!isValidSymbol(symbol)) {
            return false;
        }

        int index = context.getIndex();
        if (0 == index) {
            return true;
        }

        char previousSymbol = context.getText().charAt(index - 1);
        return SYMBOLS_VALID.contains(previousSymbol) || isValidSymbol(previousSymbol);
    }

    boolean isValidSymbol(char symbol) {
        return Character.isDigit(symbol) || Character.isAlphabetic(symbol) || (UNDERSCORE == symbol);
    }
}
