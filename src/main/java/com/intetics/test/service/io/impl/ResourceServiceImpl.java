package com.intetics.test.service.io.impl;

import com.intetics.test.service.io.ResourceService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/02/2020 23:24
 *
 * @author d.velichkevich
 */
public class ResourceServiceImpl implements ResourceService {

    private static final String PATTERN_FATAL_PREFIX = "!!! %s";

    private static final String MESSAGE_PATTERN_RESOURCE_NOT_FOUND = "Fatal error: classpath resource not found! Resource name: '%s'";


    private static final String MESSAGES_FILENAME = "messages.properties";
    private static final String PROPERTIES_FILENAME = "application.properties";


    private Properties messages;

    private Properties properties;


    public ResourceServiceImpl() {
        messages = new Properties();
        properties = new Properties();

        initialize();
    }

    private void initialize() {
        loadClasspathResource(messages, MESSAGES_FILENAME);

        File propertiesFile = new File(PROPERTIES_FILENAME);
        if (propertiesFile.exists()) {
            loadFileResource(properties, PROPERTIES_FILENAME);
        } else {
            loadClasspathResource(properties, PROPERTIES_FILENAME);
        }
    }

    /**
     * Loads properties from the '*.properties' file in classpath
     *
     * @param properties {@link Properties} consuming instance (Right Value)
     * @param resourceFilename {@link String} value of configuration filename
     * @return {@link Boolean} (primitive) value: {@code true} if configuration is loaded successfully and {@code false} otherwise
     */
    private boolean loadClasspathResource(Properties properties, String resourceFilename) {
        try (InputStream messagesStream = getClass().getClassLoader().getResourceAsStream(resourceFilename)) {
            properties.load(messagesStream);
        } catch (IOException e) {
            displayFatalError(resourceFilename);
            return false;
        }
        return true;
    }

    /**
     * Loads properties from the '*.properties' file, which is located in the working directory (current location)
     *
     * @param properties {@link Properties} consuming instance (Right Value)
     * @param resourceFilename {@link String} value of configuration filename
     * @return {@link Boolean} (primitive) value: {@code true} if configuration is loaded successfully and {@code false} otherwise
     */
    private boolean loadFileResource(Properties properties, String resourceFilename) {
        try (InputStream stream = new FileInputStream(resourceFilename)) {
            properties.load(stream);
        } catch (IOException e) {
            displayFatalError(resourceFilename);
            return false;
        }
        return true;
    }

    private void displayFatalError(String resourceName) {
        String fatalMessage = String.format(MESSAGE_PATTERN_RESOURCE_NOT_FOUND, resourceName);
        System.out.println(String.format(PATTERN_FATAL_PREFIX, fatalMessage));
    }

    @Override
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

    @Override
    public String getMessage(String key) {
        return getMessage(key, null);
    }

    @Override
    public String getMessage(String key, String defaultValue) {
        return messages.getProperty(key, defaultValue);
    }
}
