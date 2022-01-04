package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;
import de.arthurpicht.meta.config.exceptions.ConfigurationException;

public class ConfigHelper {

    public static void assertKey(Configuration configuration, String key) throws ConfigurationException {
        if (!configuration.containsKey(key))
            throw new ConfigurationException("Configuration key [" + key + "] not found in meta configuration section " +
                    "[" + configuration.getSectionName() + "].");
    }

}
