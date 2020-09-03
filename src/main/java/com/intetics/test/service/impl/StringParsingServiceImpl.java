package com.intetics.test.service.impl;

import com.intetics.test.model.StringGroup;
import com.intetics.test.model.StringParsingResult;
import com.intetics.test.service.StringGroupOrganizingStrategy;
import com.intetics.test.service.StringParsingService;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/01/2020 15:15
 *
 * @author d.velichkevich
 */
public class StringParsingServiceImpl implements StringParsingService {

    private static final String DELIMITER_TOKENS = ",";

    private static final char DELIMITER_QUOTE = '"';
    private static final char DELIMITER_GROUP_OPEN = '(';
    private static final char DELIMITER_GROUP_CLOSE = ')';


    private static final int SIZE_EMPTY = 0;
    private static final int SIZE_NO_COMMAS = 2;


    private StringGroupOrganizingStrategy organizingStrategy;


    public StringParsingServiceImpl(StringGroupOrganizingStrategy organizingStrategy) {
        setOrganizingStrategy(organizingStrategy);
    }

    private void setOrganizingStrategy(StringGroupOrganizingStrategy organizingStrategy) {
        this.organizingStrategy = (null == organizingStrategy) ? (new DefaultStringGroupOrganizingStrategy()) : (organizingStrategy);
    }

    @Override
    public StringParsingResult parse(String sourceString) {
        int openedGroups = 0;
        StringGroup root = organizingStrategy.createRoot();
        StringGroup current = root;
        StringBuilder tokenName = new StringBuilder();
        String[] tokens = sourceString.split(DELIMITER_TOKENS);
        int lastIndex = tokens.length - 1;
        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i].trim();
            if (token.isEmpty() && (tokens.length > SIZE_NO_COMMAS) && (i > 0)) {
                return new StringParsingResult(sourceString);
            }

            int lastTokenIndex = token.length() - 1;
            for (int j = 0; j < token.length(); j++) {
                char symbol = token.charAt(j);
                if (DELIMITER_QUOTE == symbol) {
                    if (((i > 0) || (j > 0)) && ((i < lastIndex) || (j < lastTokenIndex))) {
                        return new StringParsingResult(sourceString);
                    }
                } else if (DELIMITER_GROUP_OPEN == symbol) {
                    openedGroups++;
                    current = openGroup(current, tokenName);
                } else if (DELIMITER_GROUP_CLOSE == symbol) {
                    if ((SIZE_EMPTY == openedGroups) || isGroupClosingInvalid(token, j)) {
                        return new StringParsingResult(token);
                    }

                    openedGroups--;
                    current = closeGroup(current, tokenName);
                } else {
                    tokenName.append(symbol);
                }
            }
            addChildGroup(current, tokenName);
        }

        if ((openedGroups > 0) || root.getChildrenGroups().isEmpty()) {
            return new StringParsingResult(sourceString);
        }

        return new StringParsingResult(root);
    }

    private StringGroup openGroup(StringGroup current, StringBuilder tokenName) {
        if (SIZE_EMPTY == tokenName.length()) {
            return current;
        }

        current = organizingStrategy.create((current.getGroupLevel() + 1), tokenName.toString(), current);
        current.getParentGroup().getChildrenGroups().add(current);
        tokenName.setLength(SIZE_EMPTY);
        return current;
    }

    private boolean isGroupClosingInvalid(String token, int tokenSymbolIndex) {
        return (0 == tokenSymbolIndex) || ((tokenSymbolIndex > 0) && !Character.isDigit(token.charAt(tokenSymbolIndex - 1))
                && !Character.isAlphabetic(token.charAt(tokenSymbolIndex - 1)) && (DELIMITER_GROUP_CLOSE != token.charAt(tokenSymbolIndex - 1)));
    }

    private StringGroup closeGroup(StringGroup current, StringBuilder tokenName) {
        if (SIZE_EMPTY == tokenName.length()) {
            return current;
        }

        addChildGroup(current, tokenName);
        current = current.getParentGroup();
        return current;
    }

    private void addChildGroup(StringGroup current, StringBuilder tokenName) {
        if (SIZE_EMPTY == tokenName.length()) {
            return;
        }

        StringGroup group = organizingStrategy.create((current.getGroupLevel() + 1), tokenName.toString(), current);
        current.getChildrenGroups().add(group);
        tokenName.setLength(SIZE_EMPTY);
    }
}
