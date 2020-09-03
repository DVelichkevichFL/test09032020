package com.intetics.test;

import com.intetics.test.model.SortedStringGroup;
import com.intetics.test.model.StringGroup;
import com.intetics.test.model.StringParsingResult;
import com.intetics.test.service.StringGroupOrganizingStrategy;
import com.intetics.test.service.StringParsingService;
import com.intetics.test.service.impl.DefaultStringGroupOrganizingStrategy;
import com.intetics.test.service.impl.StringParsingServiceImpl;
import com.intetics.test.service.io.ConsoleService;
import com.intetics.test.service.io.ResourceService;
import com.intetics.test.service.io.impl.ConsoleServiceImpl;
import com.intetics.test.service.io.impl.ResourceServiceImpl;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/02/2020 15:51
 *
 * @author d.velichkevich
 */
public class TestApplication {

    private static final int INDEX_SOURCE_STRING = 0;

    private static final String KEY_GROUPS_ORGANIZATION_STRATEGY = "parsing.groups.organizing.strategy";

    private static final String STRATEGY_DEFAULT = "plain";


    private ConsoleService consoleService;
    private StringParsingService parser;

    private String strategyName;


    public TestApplication() {
        ResourceService resourceService = new ResourceServiceImpl();
        consoleService = new ConsoleServiceImpl(resourceService);

        strategyName = resourceService.getProperty(KEY_GROUPS_ORGANIZATION_STRATEGY, STRATEGY_DEFAULT);

        StringGroupOrganizingStrategy strategy = getStringGroupOrganizingStrategy(strategyName);
        parser = new StringParsingServiceImpl(strategy);
    }

    private StringGroupOrganizingStrategy getStringGroupOrganizingStrategy(String strategyName) {
        return (STRATEGY_DEFAULT.equals(strategyName)) ? (new DefaultStringGroupOrganizingStrategy()) : (new StringGroupOrganizingStrategy() {

            @Override
            public StringGroup createRoot() {
                return new SortedStringGroup();
            }

            @Override
            public StringGroup create(int level, String token, StringGroup parent) {
                return new SortedStringGroup(level, token, parent);
            }
        });
    }

    /**
     * The main method which executes the main application logic: parsing the input string or the default string. The string can be specified as the first command line
     * argument
     *
     * @param arguments {@link String}[] array of command line arguments. The first string in the array may be a string to parse (OPTIONAL)
     */
    public void execute(String[] arguments) {
        String sourceString = getSourceString(arguments, strategyName);
        StringParsingResult result = parser.parse(sourceString);
        if (result.isErrorOccurred()) {
            consoleService.println(ConsoleService.MESSAGE_KEY_ERROR_IN_STRING, result.getErroneousPart());
        } else {
            consoleService.generateOutput(result.getParsingResult());
        }
    }

    private String getSourceString(String[] arguments, String groupsOrganizingStrategy) {
        String result = (arguments.length > INDEX_SOURCE_STRING) ? (arguments[INDEX_SOURCE_STRING]) : (ConsoleService.STRING_EMPTY);
        return (result.isEmpty()) ? (consoleService.requestString(groupsOrganizingStrategy)) : (result);
    }


    //
    // Static Methods
    //

    /**
     * Application entry point
     *
     * @param arguments {@link String}[] array of command line arguments. The first string in the array may be a string to parse (OPTIONAL)
     */
    public static void main(String[] arguments) {
        TestApplication application = new TestApplication();
        application.execute(arguments);
    }
}
