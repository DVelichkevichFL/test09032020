package com.intetics.test.model;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/01/2020 19:44
 *
 * @author d.velichkevich
 */
public class StringParsingResult {

    private boolean errorOccurred;

    private String erroneousPart;

    private StringGroup parsingResult;


    public StringParsingResult(String erroneousPart) {
        this.erroneousPart = erroneousPart;

        errorOccurred = true;
    }

    public StringParsingResult(StringGroup parsingResult) {
        this.parsingResult = parsingResult;

        errorOccurred = false;
    }


    // Getters And Setters

    public boolean isErrorOccurred() {
        return errorOccurred;
    }

    public String getErroneousPart() {
        return erroneousPart;
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

        //noinspection SimplifiableIfStatement
        if ((null != erroneousPart) ? (!erroneousPart.equals(that.getErroneousPart())) : (null != that.getErroneousPart())) {
            return false;
        }

        return ((null != parsingResult) ? (parsingResult.equals(that.getParsingResult())) : (null != that.getParsingResult()));
    }

    @Override
    public int hashCode() {
        int result = (errorOccurred ? (1) : (0));
        result = 31 * result + ((null != erroneousPart) ? (erroneousPart.hashCode()) : (0));
        result = 31 * result + ((null != parsingResult) ? (parsingResult.hashCode()) : (0));
        return result;
    }
}
