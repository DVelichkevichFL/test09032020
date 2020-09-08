package com.intetics.test.service.impl;

import com.intetics.test.model.StringGroup;
import com.intetics.test.model.StringParsingResult;
import com.intetics.test.model.analyzer.AnalyzingContext;
import com.intetics.test.model.analyzer.AnalyzingErrorResult;
import com.intetics.test.service.StringGroupOrganizingStrategy;
import com.intetics.test.service.StringParsingService;
import com.intetics.test.service.analyzer.SymbolAnalyzer;
import com.intetics.test.service.analyzer.impl.BracketClosingAnalyzer;
import com.intetics.test.service.analyzer.impl.BracketOpeningAnalyzer;
import com.intetics.test.service.analyzer.impl.CommaAnalyzer;
import com.intetics.test.service.analyzer.impl.DefaultAnalyzer;
import com.intetics.test.service.analyzer.impl.LastSymbolAnalyzer;
import com.intetics.test.service.analyzer.impl.QuoteAnalyzer;
import com.intetics.test.service.analyzer.impl.SymbolValidityAnalyzer;
import com.intetics.test.service.analyzer.validator.SymbolValidator;
import com.intetics.test.service.analyzer.validator.impl.BracketClosingValidator;
import com.intetics.test.service.analyzer.validator.impl.BracketOpeningValidator;
import com.intetics.test.service.analyzer.validator.impl.CommaValidator;
import com.intetics.test.service.analyzer.validator.impl.NameSymbolsValidator;
import com.intetics.test.service.analyzer.validator.impl.QuoteValidator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/01/2020 15:15
 *
 * @author d.velichkevich
 */
public class StringParsingServiceImpl implements StringParsingService {

    private static final int INDEX_SOURCE_START = 0;

    private static final char SYMBOL_ZERO = '\u0000';

    private static final String STRING_EMPTY = "";

    private static final String PATTERN_WHITESPACES = "\\s*";
    private static final Pattern PATTERN_INVALID_TOKENS = Pattern.compile("[\\w]+(\\s)+[\\w]+", Pattern.CASE_INSENSITIVE);


    private StringGroupOrganizingStrategy organizingStrategy;

    private SymbolAnalyzer analyzersChain;


    StringParsingServiceImpl() {
    }

    public StringParsingServiceImpl(StringGroupOrganizingStrategy organizingStrategy) {
        this(organizingStrategy, null);
    }

    public StringParsingServiceImpl(StringGroupOrganizingStrategy organizingStrategy, SymbolAnalyzer analyzersChain) {
        setOrganizingStrategy(organizingStrategy);
        setAnalyzersChain(analyzersChain);
    }

    void setOrganizingStrategy(StringGroupOrganizingStrategy organizingStrategy) {
        this.organizingStrategy = (null == organizingStrategy) ? (new DefaultStringGroupOrganizingStrategy()) : (organizingStrategy);
    }

    void setAnalyzersChain(SymbolAnalyzer analyzersChain) {
        this.analyzersChain = (null == analyzersChain) ? (createAnalyzersChain()) : (analyzersChain);
    }

    SymbolAnalyzer createAnalyzersChain() {
        LastSymbolAnalyzer lastSymbolAnalyzer = new LastSymbolAnalyzer(null, organizingStrategy);
        DefaultAnalyzer defaultAnalyzer = new DefaultAnalyzer(lastSymbolAnalyzer);
        QuoteAnalyzer quoteAnalyzer = new QuoteAnalyzer(defaultAnalyzer);
        BracketClosingAnalyzer bracketClosingAnalyzer = new BracketClosingAnalyzer(quoteAnalyzer, organizingStrategy);
        BracketOpeningAnalyzer bracketOpeningAnalyzer = new BracketOpeningAnalyzer(bracketClosingAnalyzer, organizingStrategy);
        CommaAnalyzer commaAnalyzer = new CommaAnalyzer(bracketOpeningAnalyzer, organizingStrategy);
        return new SymbolValidityAnalyzer(commaAnalyzer, createValidators());
    }

    List<SymbolValidator> createValidators() {
        NameSymbolsValidator validator = new NameSymbolsValidator();
        return new ArrayList<>(Arrays.asList(
                validator, new CommaValidator(validator), new BracketOpeningValidator(validator), new BracketClosingValidator(validator), new QuoteValidator(validator)));
    }

    @Override
    public StringParsingResult parse(String source) {
        Matcher matcher = PATTERN_INVALID_TOKENS.matcher(source);
        if (matcher.find()) {
            return new StringParsingResult(source, new AnalyzingErrorResult((matcher.start() + 1), matcher.end()));
        }

        StringGroup root = organizingStrategy.createRoot();
        source = source.replaceAll(PATTERN_WHITESPACES, STRING_EMPTY);
        int lastIndex = source.length() - 1;
        AnalyzingContext context = new AnalyzingContext(INDEX_SOURCE_START, lastIndex, SYMBOL_ZERO, source, root);
        for (int i = INDEX_SOURCE_START; i <= lastIndex; i++) {
            updateContext(context, source, i);
            AnalyzingErrorResult errorResult = analyzersChain.analyze(context);
            if (null != errorResult) {
                return new StringParsingResult(source, errorResult);
            }
        }

        return new StringParsingResult(source, root);
    }

    void updateContext(AnalyzingContext context, String source, int index) {
        context.setIndex(index);
        context.setSymbol(source.charAt(index));
    }


    // Getters And Setters

    StringGroupOrganizingStrategy getOrganizingStrategy() {
        return organizingStrategy;
    }

    SymbolAnalyzer getAnalyzersChain() {
        return analyzersChain;
    }
}
