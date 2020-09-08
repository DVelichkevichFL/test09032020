package com.intetics.test.service.analyzer.impl;

import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.analyzer.AbstractBaseSymbolAnalyzer;
import com.intetics.test.service.analyzer.SymbolAnalyzer;
import com.intetics.test.service.analyzer.validator.SymbolValidator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 18:45
 *
 * <br /><br />This is the most important analyzer. It allows to determine, whether the current symbol is valid and it is located at the valid position.<br />
 * It MUST BE the first analyzer in the chain!<br />
 * This analyzer REQUIRES a default {@link SymbolValidator}, {@link SymbolValidator#getTargetSymbol()} of which returns {@code \u0000}
 *
 * @author d.velichkevich
 */
public class SymbolValidityAnalyzer extends AbstractBaseSymbolAnalyzer {

    static final char KEY_DEFAULT_VALIDATOR = '\u0000';


    private Map<Character, SymbolValidator> validatorsMap;


    public SymbolValidityAnalyzer(SymbolAnalyzer next) {
        super(next);
    }

    public SymbolValidityAnalyzer(SymbolAnalyzer next, List<SymbolValidator> validators) {
        super(next);

        validatorsMap = new HashMap<>();

        initialize(validators);
    }

    void initialize(List<SymbolValidator> validators) {
        validators.forEach((SymbolValidator validator) -> validatorsMap.put(validator.getTargetSymbol(), validator));
    }

    @Override
    public AnalyzingErrorResult analyze(AnalyzingContext context) {
        char symbol = context.getSymbol();
        SymbolValidator validator = (validatorsMap.containsKey(symbol)) ? (validatorsMap.get(symbol)) : (validatorsMap.get(KEY_DEFAULT_VALIDATOR));
        if (validator.isValid(context)) {
            return analyzeNext(context);
        }

        return new AnalyzingErrorResult(symbol, (context.getIndex() + 1));
    }


    // Getters And Setters

    public Map<Character, SymbolValidator> getValidatorsMap() {
        return validatorsMap;
    }

    void setValidatorsMap(Map<Character, SymbolValidator> validatorsMap) {
        this.validatorsMap = validatorsMap;
    }
}
