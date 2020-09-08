package com.intetics.test.model.analyzer;

import com.intetics.test.model.StringGroup;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/05/2020 17:15
 *
 * @author d.velichkevich
 */
public class AnalyzingContext {

    public static final int COUNT_MAXIMUM_QUOTES = 2;


    // Initially set from an external source

    private int index;

    private int lastIndex;

    private char symbol;

    private String text;

    private StringGroup currentGroup;


    // Calculated

    private int bracketsCount;

    private int availableQuotes;

    private StringBuilder tokenName;


    public AnalyzingContext(int index, int lastIndex, char symbol, String text, StringGroup currentGroup) {
        this.index = index;
        this.lastIndex = lastIndex;
        this.symbol = symbol;
        this.text = text;
        this.currentGroup = currentGroup;

        bracketsCount = 0;
        availableQuotes = COUNT_MAXIMUM_QUOTES;
        tokenName = new StringBuilder();
    }

    public AnalyzingContext(char newSymbol, int index, AnalyzingContext source) {
        setIndex(index);
        lastIndex = source.getLastIndex();
        setSymbol(newSymbol);
        text = source.getText();
        setCurrentGroup(source.getCurrentGroup());

        setBracketsCount(source.getBracketsCount());
        setAvailableQuotes(source.getAvailableQuotes());
        setTokenName(source.getTokenName());
    }


    // Getters And Setters

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getLastIndex() {
        return lastIndex;
    }

    public char getSymbol() {
        return symbol;
    }

    public void setSymbol(char symbol) {
        this.symbol = symbol;
    }

    public String getText() {
        return text;
    }

    public StringGroup getCurrentGroup() {
        return currentGroup;
    }

    public void setCurrentGroup(StringGroup currentGroup) {
        this.currentGroup = currentGroup;
    }

    public int getBracketsCount() {
        return bracketsCount;
    }

    public void setBracketsCount(int bracketsCount) {
        this.bracketsCount = bracketsCount;
    }

    public int getAvailableQuotes() {
        return availableQuotes;
    }

    public void setAvailableQuotes(int availableQuotes) {
        this.availableQuotes = availableQuotes;
    }

    public StringBuilder getTokenName() {
        return tokenName;
    }

    public void setTokenName(StringBuilder tokenName) {
        this.tokenName = tokenName;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if ((null == o) || (getClass() != o.getClass())) {
            return false;
        }

        AnalyzingContext that = (AnalyzingContext) o;

        if (index != that.getIndex()) {
            return false;
        }

        if (that.getLastIndex() != lastIndex) {
            return false;
        }

        if (that.getSymbol() != symbol) {
            return false;
        }

        if (!that.getText().equals(text)) {
            return false;
        }

        if (!that.getCurrentGroup().equals(currentGroup)) {
            return false;
        }

        if (that.getBracketsCount() != bracketsCount) {
            return false;
        }

        //noinspection SimplifiableIfStatement
        if (availableQuotes != that.getAvailableQuotes()) {
            return false;
        }

        return tokenName.toString().equals(that.getTokenName().toString());
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + lastIndex;
        result = 31 * result + (int) symbol;
        result = 31 * result + text.hashCode();
        result = 31 * result + currentGroup.hashCode();
        result = 31 * result + bracketsCount;
        result = 31 * result + availableQuotes;
        result = 31 * result + tokenName.hashCode();
        return result;
    }
}
