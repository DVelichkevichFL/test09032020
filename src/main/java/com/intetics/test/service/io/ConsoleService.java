package com.intetics.test.service.io;

import com.intetics.test.model.StringGroup;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/02/2020 19:00
 *
 * @author d.velichkevich
 */
public interface ConsoleService {

    String STRING_EMPTY = "";

    String MESSAGE_KEY_ERROR_IN_STRING = "message.output.error.in.string";


    /**
     * Asks the user to enter a string to parse
     *
     * @param groupsOrganizingStrategy {@link String} value of groups organizing strategy
     * @return {@link String} value of the entered string or the default string to parse. It never returns {@code null} or an empty string!
     */
    String requestString(String groupsOrganizingStrategy);

    /**
     * Generates output for a string groups hierarchy. The tokens of each group are sorted alphabetically. Each group (except for the root group) has a prefix of "-"
     * symbols, the length of which depends on the hierarchy level minus one
     *
     * @param result {@link StringGroup} string groups hierarchy instance
     */
    void generateOutput(StringGroup result);

    /**
     * Prints the message, obtained from the message properties configuration, and moves the caret to the next line. The {@code messageKey} may point to a pattern, which
     * should be completed with {@code messageParameters}
     *
     * @param messageKey {@link String} message key value in message properties configuration
     * @param messageParameters {@link Object}... collection of message pattern parameters (OPTIONAL)
     */
    void println(String messageKey, Object... messageParameters);
}
