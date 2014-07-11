package org.jackalope.study.conf.common;

import com.google.common.collect.Maps;

import java.io.File;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public abstract class AbstractConfigurationProvider {

    protected final String agentName;
    protected final File file;

    public AbstractConfigurationProvider(String agentName, File file){
        this.agentName = agentName;
        this.file = file;
    }

    protected Map<String, String> toMap(Properties properties) {
        Map<String, String> result = Maps.newHashMap();
        Enumeration<?> propertyNames = properties.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String name = (String) propertyNames.nextElement();
            String value = properties.getProperty(name);
            result.put(name, value);
        }
        return result;
    }

    protected abstract AbstractConfiguration getConfiguration();
}
