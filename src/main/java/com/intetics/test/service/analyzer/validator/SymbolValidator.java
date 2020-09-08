package com.intetics.test.service.analyzer.validator;

import com.intetics.test.model.analyzer.AnalyzingContext;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 17:01
 *
 * @author d.velichkevich
 *
 * Important: whitespaces are invalid symbols! They should be removed from the string, except for whitespaces between symbols in token name
 */
public interface SymbolValidator {

    char QUOTE = '"';

    char BRACKET_OPENING = '(';
    char BRACKET_CLOSING = ')';

    char COMMA = ',';

    char UNDERSCORE = '_';

    Set<Character> SYMBOLS_ALL_SPECIALS = new HashSet<>(Arrays.asList(QUOTE, BRACKET_OPENING, BRACKET_CLOSING, COMMA));


    /**
     * Determines for which symbol the given validator exists
     *
     * @return {@link Character} (primitive) value
     */
    char getTargetSymbol();

    /**
     * Checks, if the current symbol is valid for the current {@code context}
     *
     * @param context {@link AnalyzingContext} instance of the current context
     * @return {@link Boolean} (primitive) value: {@code true} if the current symbol is valid for the current context, and {@code false} otherwise
     */
    boolean isValid(AnalyzingContext context);
}
