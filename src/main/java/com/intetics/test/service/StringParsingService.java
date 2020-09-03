package com.intetics.test.service;

import com.intetics.test.model.StringParsingResult;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/02/2020 19:05
 *
 * @author d.velichkevich
 */
public interface StringParsingService {

    /**
     * Parses the specified {@code sourceString} and represents the encoded structure as a hierarchy of string groups. The specified string is checked for validity during
     * parsing. The result may contain the erroneous part of the {@code sourceString} or the entire {@code sourceString}, if the data integrity is violated
     *
     * @param sourceString {@link String} value of the string to parse
     * @return {@link StringParsingResult} instance, which contains a hierarchy of string groups or an erroneous part or the entire string, if the data integrity is
     * violated
     */
    StringParsingResult parse(String sourceString);
}
