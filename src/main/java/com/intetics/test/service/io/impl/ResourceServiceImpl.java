package com.intetics.test.service.io.impl;

import com.intetics.test.service.io.ResourceService;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Properties;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/02/2020 23:24
 *
 * @author d.velichkevich
 */
public class ResourceServiceImpl implements ResourceService {

    static final String MESSAGE_PATTERN_RESOURCE_NOT_FOUND = "Fatal error: classpath resource not found! Resource name: '%s'";

    static final String MESSAGES_FILENAME = "messages.properties";
    static final String PROPERTIES_FILENAME = "application.properties";

    private static final String PATTERN_FATAL_PREFIX = "!!! %s";


    private String messagesFilename;
    private String propertiesFilename;

    private Properties messages;

    private Properties properties;


    public ResourceServiceImpl() {
        this(MESSAGES_FILENAME, PROPERTIES_FILENAME);

        initialize();
    }

    public ResourceServiceImpl(String messagesFilename, String propertiesFilename) {
        this.messagesFilename = messagesFilename;
        this.propertiesFilename = propertiesFilename;

        messages = new Properties();
        properties = new Properties();
    }

    void initialize() {
        loadClasspathResource(messages, messagesFilename);

        File propertiesFile = new File(propertiesFilename);
        if (propertiesFile.exists()) {
            loadFileResource(properties, propertiesFilename);
        } else {
            loadClasspathResource(properties, propertiesFilename);
        }
    }

    /**
     * Loads properties from the '*.properties' file in classpath
     *
     * @param properties {@link Properties} consuming instance (Right Value)
     * @param resourceFilename {@link String} value of configuration filename
     */
    void loadClasspathResource(Properties properties, String resourceFilename) {
        try (InputStream messagesStream = getClasspathResourceInputStream(resourceFilename)) {
            properties.load(messagesStream);
        } catch (IOException e) {
            displayFatalError(resourceFilename);
            throw new RuntimeException(e);
        }
    }

    InputStream getClasspathResourceInputStream(String resourceFilename) {
        return getClass().getClassLoader().getResourceAsStream(resourceFilename);
    }

    /**
     * Loads properties from the '*.properties' file, which is located in the working directory (current location)
     *
     * @param properties {@link Properties} consuming instance (Right Value)
     * @param resourceFilename {@link String} value of configuration filename
     */
    void loadFileResource(Properties properties, String resourceFilename) {
        try (InputStream stream = new FileInputStream(resourceFilename)) {
            properties.load(stream);
        } catch (IOException e) {
            displayFatalError(resourceFilename);
            throw new RuntimeException(e);
        }
    }

    void displayFatalError(String resourceName) {
        String fatalMessage = String.format(MESSAGE_PATTERN_RESOURCE_NOT_FOUND, resourceName);
        getConsoleOutputStream().println(String.format(PATTERN_FATAL_PREFIX, fatalMessage));
    }

    PrintStream getConsoleOutputStream() {
        return System.out;
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


    // Getters And Setters

    String getMessagesFilename() {
        return messagesFilename;
    }

    String getPropertiesFilename() {
        return propertiesFilename;
    }

    Properties getMessages() {
        return messages;
    }

    void setMessages(Properties messages) {
        this.messages = messages;
    }

    Properties getProperties() {
        return properties;
    }

    void setProperties(Properties properties) {
        this.properties = properties;
    }
}
