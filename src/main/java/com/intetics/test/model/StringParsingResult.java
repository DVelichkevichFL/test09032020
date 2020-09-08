package com.intetics.test.model;

import com.intetics.test.model.analyzer.AnalyzingErrorResult;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/01/2020 19:44
 *
 * @author d.velichkevich
 */
public class StringParsingResult {

    private boolean errorOccurred;

    private String preProcessedString;

    private AnalyzingErrorResult errorResult;

    private StringGroup parsingResult;


    protected StringParsingResult(boolean errorOccurred, String preProcessedString) {
        this.errorOccurred = errorOccurred;
        this.preProcessedString = preProcessedString;
    }

    public StringParsingResult(String preProcessedString, AnalyzingErrorResult errorResult) {
        this(true, preProcessedString);

        this.errorResult = errorResult;
    }

    public StringParsingResult(String preProcessedString, StringGroup parsingResult) {
        this(false, preProcessedString);

        this.parsingResult = parsingResult;
    }


    // Getters And Setters

    public boolean isErrorOccurred() {
        return errorOccurred;
    }

    public String getPreProcessedString() {
        return preProcessedString;
    }

    public AnalyzingErrorResult getErrorResult() {
        return errorResult;
    }

    public StringGroup getParsingResult() {
        return parsingResult;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if ((null == o) || (getClass() != o.getClass())) {
            return false;
        }

        StringParsingResult that = (StringParsingResult) o;

        if (isErrorOccurred() != that.isErrorOccurred()) {
            return false;
        }

        if ((null != errorResult) ? (!errorResult.equals(that.getErrorResult())) : (null != that.getErrorResult())) {
            return false;
        }

        //noinspection SimplifiableIfStatement
        if ((null != preProcessedString) ? (!preProcessedString.equals(that.getPreProcessedString())) : (null != that.getPreProcessedString())) {
            return false;
        }

        return (null != parsingResult) ? (parsingResult.equals(that.getParsingResult())) : (null == that.getParsingResult());
    }

    @Override
    public int hashCode() {
        int result = (errorOccurred ? (1) : (0));
        result = 31 * result + ((null != preProcessedString) ? (preProcessedString.hashCode()) : (0));
        result = 31 * result + ((null != errorResult) ? (errorResult.hashCode()) : (0));
        result = 31 * result + ((null != parsingResult) ? (parsingResult.hashCode()) : (0));
        return result;
    }
}
