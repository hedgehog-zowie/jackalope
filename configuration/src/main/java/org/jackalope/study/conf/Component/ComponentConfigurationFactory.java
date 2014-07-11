package org.jackalope.study.conf.component;

import org.jackalope.study.conf.client.ClientConfiguration.ClientConfigurationType;
import org.jackalope.study.conf.exception.ConfigurationException;
import org.jackalope.study.conf.iplib.IpLibConfiguration.IpLibConfigurationType;
import org.jackalope.study.conf.server.ServerConfiguration.ServerConfigurationType;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class ComponentConfigurationFactory {
    public static ComponentConfiguration create(String name, String type, ComponentType component) throws ConfigurationException {

        Class<? extends ComponentConfiguration> confType = null;

        if (type == null) {
            throw new ConfigurationException(
                    "Cannot create component without knowing its type!");
        }
        try {
            confType = (Class<? extends ComponentConfiguration>) Class.forName(type);
            return confType.getConstructor(String.class).newInstance(type);
        } catch (Exception ignored) {
            try {
                type = type.toUpperCase();
                switch (component) {
                    case SERVER:
                        return ServerConfigurationType.valueOf(type.toUpperCase()).getConfiguration(name);
                    case CLIENT:
                        return ClientConfigurationType.valueOf(type.toUpperCase()).getConfiguration(name);
                    case IPLIB:
                        return IpLibConfigurationType.valueOf(type.toUpperCase()).getConfiguration(name);
                    default:
                        throw new ConfigurationException("Cannot create configuration. Unknown Type specified: " + type);
                }
            } catch (ConfigurationException e) {
                throw e;
            } catch (Exception e) {
                throw new ConfigurationException("Could not create configuration! " +
                        " Due to " + e.getClass().getSimpleName() + ": " + e.getMessage(), e);
            }
        }

    }
}
