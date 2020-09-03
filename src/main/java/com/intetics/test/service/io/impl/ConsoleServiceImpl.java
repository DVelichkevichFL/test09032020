package com.intetics.test.service.io.impl;

import com.intetics.test.model.StringGroup;
import com.intetics.test.service.io.ConsoleService;
import com.intetics.test.service.io.ResourceService;
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

    private static final String PREFIX_ITEM = "-";

    private static final String DELIMITER_SPACE = " ";

    private static final String PATTERN_OUTPUT = ">>> %s";
    private static final String PATTERN_CLEAN_OUTPUT = "%s";
    private static final String PATTERN_INPUT = "<<< %s";

    private static final String MESSAGE_KEY_DEFAULT_STRING = "message.default.string";
    private static final String MESSAGE_KEY_ENTER_STRING = "message.input.enter.string";

    private static final String MESSAGE_KEY_RESULT = "message.output.result";


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

        Scanner inputReader = new Scanner(System.in);
        String result = inputReader.nextLine();
        return (result.isEmpty()) ? (defaultString) : (result);
    }

    private void print(String prefixPattern, String messageKey, Object... messageParameters) {
        String message = getOutputMessage(messageKey, messageParameters);
        System.out.print(String.format(prefixPattern, message));
    }

    @Override
    public void generateOutput(StringGroup result) {
        Stack<StringGroup> groups = new Stack<>();
        addAllGroups(groups, result.getChildrenGroups());
        while (!groups.empty()) {
            StringGroup group = groups.pop();
            printlnImpl(PATTERN_CLEAN_OUTPUT, MESSAGE_KEY_RESULT, generatePrefix(group.getGroupLevel()), group.getToken());
            addAllGroups(groups, group.getChildrenGroups());
        }
    }

    private void addAllGroups(Stack<StringGroup> groups, Collection<StringGroup> children) {
        List<StringGroup> list = new ArrayList<>(children);
        Collections.reverse(list);
        list.forEach(groups::push);
    }

    private void printlnImpl(String prefixPattern, String messageKey, Object... messageParameters) {
        String message = getOutputMessage(messageKey, messageParameters);
        System.out.println(String.format(prefixPattern, message));
    }

    private String getOutputMessage(String messageKey, Object... messageParameters) {
        String messagePattern = resourceService.getMessage(messageKey);
        return String.format(messagePattern, messageParameters);
    }

    private String generatePrefix(int length) {
        String result = String.join(STRING_EMPTY, Collections.nCopies(length, PREFIX_ITEM));
        return result + ((result.isEmpty()) ? (STRING_EMPTY) : (DELIMITER_SPACE));
    }
}
