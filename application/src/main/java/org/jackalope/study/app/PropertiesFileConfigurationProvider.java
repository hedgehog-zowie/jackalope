package org.jackalope.study.app;

import com.google.common.collect.Maps;
import com.iuni.data.avro.ClientFactory;
import com.iuni.data.avro.DefaultClientFactory;
import com.iuni.data.avro.DefaultServerFactory;
import com.iuni.data.avro.ServerFactory;
import org.jackalope.study.conf.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesFileConfigurationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(PropertiesFileConfigurationProvider.class);

    private final File file;
    private final String agentName;
    private final ClientFactory clientFactory;
    private final ServerFactory serverFactory;

    public PropertiesFileConfigurationProvider(String agentName, File file) {
        this.agentName = agentName;
        this.file = file;
        this.clientFactory = new DefaultClientFactory();
        this.serverFactory = new DefaultServerFactory();
    }

    public Configuration getConfiguration() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            Properties properties = new Properties();
            properties.load(reader);
            return new Configuration(toMap(properties));
        } catch (IOException ex) {
            LOGGER.error("Unable to load file:" + file
                    + " (I/O failure) - Exception follows.", ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    LOGGER.warn(
                            "Unable to close file reader for file: " + file, ex);
                }
            }
        }
        return new Configuration(new HashMap<String, String>());
    }

    private Map<String, String> toMap(Properties properties) {
        Map<String, String> result = Maps.newHashMap();
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String name = (String) propertyNames.nextElement();
            String value = properties.getProperty(name);
            result.put(name, value);
        }
        return result;
    }
}
