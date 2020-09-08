package com.intetics.test.model.analyzer;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 17:31
 *
 * @author d.velichkevich
 */
public class AnalyzingErrorResult {

    private char unexpectedSymbol;

    private int unexpectedSymbolIndex;

    private int invalidTokenEndIndex;

    private AnalyzingStringErrorEnum errorType;


    public AnalyzingErrorResult(AnalyzingStringErrorEnum errorType) {
        this.errorType = errorType;

        unexpectedSymbol = '\u0000';
        unexpectedSymbolIndex = 0;
        invalidTokenEndIndex = 0;
    }

    public AnalyzingErrorResult(char unexpectedSymbol, int unexpectedSymbolIndex) {
        this(AnalyzingStringErrorEnum.UNEXPECTED_SYMBOL_AT_INDEX);

        this.unexpectedSymbol = unexpectedSymbol;
        this.unexpectedSymbolIndex = unexpectedSymbolIndex;
    }

    public AnalyzingErrorResult(int unexpectedSymbolIndex, int invalidTokenEndIndex) {
        this(AnalyzingStringErrorEnum.INVALID_TOKEN);

        this.unexpectedSymbolIndex = unexpectedSymbolIndex;
        this.invalidTokenEndIndex = invalidTokenEndIndex;
    }

    public AnalyzingErrorResult(char unexpectedSymbol, int unexpectedSymbolIndex, int invalidTokenEndIndex, AnalyzingStringErrorEnum errorType) {
        this.unexpectedSymbol = unexpectedSymbol;
        this.unexpectedSymbolIndex = unexpectedSymbolIndex;
        this.invalidTokenEndIndex = invalidTokenEndIndex;
        this.errorType = errorType;
    }


    // Getters And Setters

    public char getUnexpectedSymbol() {
        return unexpectedSymbol;
    }

    public int getUnexpectedSymbolIndex() {
        return unexpectedSymbolIndex;
    }

    public int getInvalidTokenEndIndex() {
        return invalidTokenEndIndex;
    }

    public AnalyzingStringErrorEnum getErrorType() {
        return errorType;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if ((null == o) || (getClass() != o.getClass())) {
            return false;
        }

        AnalyzingErrorResult that = (AnalyzingErrorResult) o;

        if (that.getUnexpectedSymbol() != unexpectedSymbol) {
            return false;
        }

        if (that.getUnexpectedSymbolIndex() != unexpectedSymbolIndex) {
            return false;
        }

        //noinspection SimplifiableIfStatement
        if (that.getInvalidTokenEndIndex() != invalidTokenEndIndex) {
            return false;
        }

        return that.getErrorType() == errorType;
    }

    @Override
    public int hashCode() {
        int result = (int) getUnexpectedSymbol();
        result = 31 * result + unexpectedSymbolIndex;
        result = 31 * result + invalidTokenEndIndex;
        result = 31 * result + errorType.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AnalyzingErrorResult{" +
                "unexpectedSymbol=" + unexpectedSymbol +
                ", unexpectedSymbolIndex=" + unexpectedSymbolIndex +
                ", invalidTokenEndIndex=" + invalidTokenEndIndex +
                ", errorType=" + errorType +
                '}';
    }
}
