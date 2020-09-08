package com.intetics.test.model.analyzer;

import com.intetics.test.model.StringParsingResult;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/06/2020 0:18
 *
 * @author d.velichkevich
 */
public enum AnalyzingStringErrorEnum {

    INVALID_TOKEN("message.output.error.invalid.token") {

        @Override
        public Object[] getMessageParameters(StringParsingResult parsingResult) {
            return new Object[] {
                parsingResult.getErrorResult().getUnexpectedSymbolIndex(),
                parsingResult.getErrorResult().getInvalidTokenEndIndex()
            };
        }
    },

    UNEXPECTED_SYMBOL_AT_INDEX("message.output.error.unexpected.symbol.at.index") {

        @Override
        public Object[] getMessageParameters(StringParsingResult parsingResult) {
            return new Object[] {
                parsingResult.getPreProcessedString(),
                parsingResult.getErrorResult().getUnexpectedSymbol(),
                parsingResult.getErrorResult().getUnexpectedSymbolIndex()
            };
        }
    },

    NOT_ALL_GROUPS_CLOSED("message.output.error.not.all.groups.closed"),

    FINAL_QUOTATION_MARK_MISSING("message.output.error.final.quotation.mark.missing");


    private String errorKey;


    AnalyzingStringErrorEnum(String errorKey) {
        this.errorKey = errorKey;
    }

    /**
     * Processes the specified {@link StringParsingResult} instance in context of parameters for the current message key
     *
     * @param parsingResult {@link StringParsingResult} instance
     * @return {@link Object}[] array of the message parameters
     */
    public Object[] getMessageParameters(StringParsingResult parsingResult) {
        return new Object[0];
    }


    // Getters And Setters

    public String getErrorKey() {
        return errorKey;
    }
}
