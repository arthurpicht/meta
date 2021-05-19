package de.arthurpicht.meta.config;

import de.arthurpicht.configuration.Configuration;

public class ConfigHelper {

    public static void assertKey(Configuration configuration, String section, String key) throws ConfigurationException {
        if (!configuration.containsKey(key))
            throw new ConfigurationException("Configuration key [" + key + "] not found in meta configuration section [" + section + "].");
    }

}
