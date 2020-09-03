package com.intetics.test.service.io;

/**
 * Revision Info : $Author$ $Date$
 * Author  : d.velichkevich
 * Created : 09/02/2020 23:16
 *
 * @author d.velichkevich
 */
public interface ResourceService {

    /**
     * Gets an application property. Configuration is loaded from filesystem or from classpath
     *
     * @param key {@link String} value of the property key
     * @param defaultValue {@link String} value, if property is not found
     * @return {@link String} value of the actual property or the {@code defaultValue}
     */
    String getProperty(String key, String defaultValue);

    /**
     * Gets an application property. Configuration is loaded from classpath
     *
     * @param key {@link String} value of the property key
     * @return {@link String} value of the actual message
     */
    String getMessage(String key);

    /**
     * Gets an application property. Configuration is loaded from classpath
     *
     * @param key {@link String} value of the property key
     * @param defaultValue {@link String} value, if message is not found
     * @return {@link String} value of the actual message or the {@code defaultValue}
     */
    String getMessage(String key, String defaultValue);
}
