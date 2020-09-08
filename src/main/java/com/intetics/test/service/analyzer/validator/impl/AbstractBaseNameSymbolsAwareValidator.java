package com.intetics.test.service.analyzer.validator.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.service.analyzer.validator.SymbolValidator;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/06/2020 21:07
 *
 * @author d.velichkevich
 */
public abstract class AbstractBaseNameSymbolsAwareValidator implements SymbolValidator {

    private SymbolValidator nameSymbolsValidator;


    public AbstractBaseNameSymbolsAwareValidator(SymbolValidator nameSymbolsValidator) {
        this.nameSymbolsValidator = nameSymbolsValidator;
    }

    /**
     * Creates a new instance of {@link AnalyzingContext} from the specified {@code context}
     *
     * @param context {@link AnalyzingContext} source context instance
     * @param previousSymbol {@link Character} (primitive) value of the new symbol
     * @return {@link AnalyzingContext} instance
     */
    protected AnalyzingContext createNewContext(AnalyzingContext context, char previousSymbol) {
        return new AnalyzingContext(previousSymbol, 0, context);
    }


    // Getters And Setters

    public SymbolValidator getNameSymbolsValidator() {
        return nameSymbolsValidator;
    }
}
