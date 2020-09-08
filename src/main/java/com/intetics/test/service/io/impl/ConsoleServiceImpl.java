package com.intetics.test.service.io.impl;

import com.intetics.test.model.StringGroup;
import com.intetics.test.service.io.ConsoleService;
import com.intetics.test.service.io.ResourceService;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/01/2020 17:06
 *
 * @author d.velichkevich
 */
public class ConsoleServiceImpl implements ConsoleService {

    static final String PREFIX_ITEM = "-";

    static final String DELIMITER_SPACE = " ";

    static final String PATTERN_OUTPUT = ">>> %s";
    static final String PATTERN_INPUT = "<<< %s";

    static final String PATTERN_CLEAN_OUTPUT = "%s";

    static final String MESSAGE_KEY_DEFAULT_STRING = "message.default.string";
    static final String MESSAGE_KEY_ENTER_STRING = "message.input.enter.string";
    static final String MESSAGE_KEY_RESULT = "message.output.result";


    private ResourceService resourceService;


    public ConsoleServiceImpl(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Override
    public void println(String messageKey, Object... messageParameters) {
        printlnImpl(PATTERN_OUTPUT, messageKey, messageParameters);
    }

    @Override
    public String requestString(String groupsOrganizingStrategy) {
        String defaultString = resourceService.getMessage(MESSAGE_KEY_DEFAULT_STRING);
        print(PATTERN_INPUT, MESSAGE_KEY_ENTER_STRING, defaultString, groupsOrganizingStrategy);

        Scanner inputReader = new Scanner(getConsoleInputStream());

        String result = inputReader.nextLine();
        return (result.isEmpty()) ? (defaultString) : (result);
    }

    void print(String prefixPattern, String messageKey, Object... messageParameters) {
        String message = getOutputMessage(messageKey, messageParameters);
        getConsoleOutputStream().print(String.format(prefixPattern, message));
    }

    InputStream getConsoleInputStream() {
        return System.in;
    }

    @Override
    public void generateOutput(StringGroup result) {
        Stack<StringGroup> groups = new Stack<>();
        groups = addAllGroups(groups, result.getChildrenGroups());
        while (!groups.empty()) {
            groups = processGroup(groups);
        }
    }

    Stack<StringGroup> processGroup(Stack<StringGroup> groups) {
        StringGroup group = groups.pop();
        printlnImpl(PATTERN_CLEAN_OUTPUT, MESSAGE_KEY_RESULT, generatePrefix(group.getGroupLevel()), group.getToken());
        return addAllGroups(groups, group.getChildrenGroups());
    }

    Stack<StringGroup> addAllGroups(Stack<StringGroup> groups, Collection<StringGroup> children) {
        List<StringGroup> list = new ArrayList<>(children);
        Collections.reverse(list);
        list.forEach(groups::push);
        return groups;
    }

    void printlnImpl(String prefixPattern, String messageKey, Object... messageParameters) {
        String message = getOutputMessage(messageKey, messageParameters);
        getConsoleOutputStream().println(String.format(prefixPattern, message));
    }

    /**
     * Gets the console output stream
     *
     * @return {@link PrintStream} instance
     */
    protected PrintStream getConsoleOutputStream() {
        return System.out;
    }

    String getOutputMessage(String messageKey, Object... messageParameters) {
        String messagePattern = resourceService.getMessage(messageKey);
        return String.format(messagePattern, messageParameters);
    }

    String generatePrefix(int length) {
        String result = String.join(STRING_EMPTY, Collections.nCopies(length, PREFIX_ITEM));
        return result + ((result.isEmpty()) ? (STRING_EMPTY) : (DELIMITER_SPACE));
    }
}
