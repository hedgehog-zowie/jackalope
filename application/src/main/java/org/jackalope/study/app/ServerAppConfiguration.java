package org.jackalope.study.app;

import org.jackalope.study.conf.common.AbstractAgentConfiguration;
import org.jackalope.study.conf.common.AbstractConfiguration;
import org.jackalope.study.conf.common.BasicConfigurationConstants;
import org.jackalope.study.conf.common.Context;
import org.jackalope.study.conf.component.ComponentConfiguration;
import org.jackalope.study.conf.component.ComponentConfigurationFactory;
import org.jackalope.study.conf.component.ComponentType;
import org.jackalope.study.conf.exception.ConfigurationError;
import org.jackalope.study.conf.exception.ConfigurationError.ErrorOrWarning;
import org.jackalope.study.conf.exception.ConfigurationErrorType;
import org.jackalope.study.conf.exception.ConfigurationException;
import org.jackalope.study.conf.server.ServerConfiguration;
import org.jackalope.study.conf.server.ServerType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class ServerAppConfiguration extends AbstractConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ServerAppConfiguration.class);

    private final Map<String, AgentConfiguration> agentConfigMap;

    public ServerAppConfiguration(Map<String, String> properties) {
        super();
        agentConfigMap = new HashMap<String, AgentConfiguration>();
        for (String name : properties.keySet()) {
            String value = properties.get(name);
            if (!addRawProperty(name, value)) {
                logger.warn("Configuration property ignored: " + name + " = " + value);
            }
        }
        validateConfiguration();
    }

//    public List<ConfigurationError> getConfigurationErrors() {
//        return errors;
//    }
//
//    public AgentConfiguration getConfigurationFor(String hostname) {
//        return agentConfigMap.get(hostname);
//    }

    @Override
    protected boolean addRawProperty(String name, String value) {
        if (name == null || value == null) {
            errors.add(new ConfigurationError("", "",
                    ConfigurationErrorType.AGENT_NAME_MISSING,
                    ErrorOrWarning.ERROR));
            return false;
        }

        if (value.trim().length() == 0) {
            errors.add(new ConfigurationError(name, "",
                    ConfigurationErrorType.PROPERTY_VALUE_NULL,
                    ErrorOrWarning.ERROR));
            return false;
        }

        name = name.trim();
        value = value.trim();

        int index = name.indexOf('.');

        if (index == -1) {
            errors.add(new ConfigurationError(name, "",
                    ConfigurationErrorType.AGENT_NAME_MISSING,
                    ErrorOrWarning.ERROR));
            return false;
        }

        String agentName = name.substring(0, index);

        if (agentName.length() == 0) {
            errors.add(new ConfigurationError(name, "",
                    ConfigurationErrorType.AGENT_NAME_MISSING,
                    ErrorOrWarning.ERROR));
            return false;
        }

        String configKey = name.substring(index + 1);

        if (configKey.length() == 0) {
            errors.add(new ConfigurationError(name, "",
                    ConfigurationErrorType.PROPERTY_NAME_NULL,
                    ErrorOrWarning.ERROR));
            return false;
        }

        AgentConfiguration sconf = agentConfigMap.get(agentName);

        if (sconf == null) {
            sconf = new AgentConfiguration(agentName, errors);
            agentConfigMap.put(agentName, sconf);
        }

        return sconf.addProperty(configKey, value);
    }

    @Override
    protected void validateConfiguration() {
        Iterator<String> it = agentConfigMap.keySet().iterator();

        while (it.hasNext()) {
            String appName = it.next();
            AgentConfiguration aconf = agentConfigMap.get(appName);

            if (!aconf.isValid()) {
                logger.warn("App configuration invalid for app '" + appName + "'. It will be removed.");
                errors.add(new ConfigurationError(appName, "",
                        ConfigurationErrorType.AGENT_CONFIGURATION_INVALID,
                        ErrorOrWarning.ERROR));
                it.remove();
            }
            logger.debug("Channels:" + aconf.getComponents() + "\n");
        }

        logger.info("Post-validation configuration contains configuration"
                + " for agents: " + agentConfigMap.keySet());
    }

    public static class AgentConfiguration extends AbstractAgentConfiguration {

        private AgentConfiguration(String agentName, List<ConfigurationError> errorList) {
            super(agentName, errorList);
        }

        @Override
        protected boolean addProperty(String key, String value) {
            if (key.equals(BasicConfigurationConstants.CONFIG_SERVERS)) {
                if (components == null) {
                    components = value;
                    return true;
                } else {
                    logger.warn("Duplicate iplib list specified for agent: " + agentName);
                    errorList.add(new ConfigurationError(agentName,
                            BasicConfigurationConstants.CONFIG_SERVERS,
                            ConfigurationErrorType.DUPLICATE_PROPERTY,
                            ErrorOrWarning.ERROR));
                    return false;
                }
            }

            ComponentNameAndConfigKey cnck = parseConfigKey(key, BasicConfigurationConstants.CONFIG_SERVERS_PREFIX);

            if (cnck != null) {
                // it is a source
                String name = cnck.getComponentName();
                Context srcConf = componentContextMap.get(name);

                if (srcConf == null) {
                    srcConf = new Context();
                    componentContextMap.put(name, srcConf);
                }

                srcConf.put(cnck.getConfigKey(), value);
                return true;
            }

            logger.warn("Invalid property specified: " + key);
            errorList.add(new ConfigurationError(agentName, key, ConfigurationErrorType.INVALID_PROPERTY, ErrorOrWarning.ERROR));
            return false;
        }

        @Override
        protected boolean isValid() {
            logger.debug("Starting validation of configuration for agent: "
                    + agentName + ", initial-configuration: " + this.getPrevalidationConfig());

            // Make sure that at least one channel is specified
            if (components == null || components.trim().length() == 0) {
                logger.warn("Agent configuration for '" + agentName
                        + "' does not contain any servers. Marking it as invalid.");
                errorList.add(new ConfigurationError(agentName,
                        BasicConfigurationConstants.CONFIG_SERVERS,
                        ConfigurationErrorType.PROPERTY_VALUE_NULL,
                        ErrorOrWarning.ERROR));
                return false;
            }

            componentSet = new HashSet<String>(Arrays.asList(components.split("\\s+")));

            componentSet = validateConponents(componentSet);
            if (componentSet.size() == 0) {
                logger.warn("Agent configuration for '" + agentName
                        + "' does not contain any valid servers. Marking it as invalid.");
                errorList.add(new ConfigurationError(agentName,
                        BasicConfigurationConstants.CONFIG_SERVERS,
                        ConfigurationErrorType.PROPERTY_VALUE_NULL,
                        ErrorOrWarning.ERROR));
                return false;
            }

            this.components = getSpaceDelimitedList(componentSet);

            logger.debug("Post validation configuration for " + agentName + "\t"
                    + this.getPostvalidationConfig());

            return true;
        }

        @Override
        protected Set<String> validateConponents(Set<String> componentSet) {
            Iterator<String> iter = componentSet.iterator();
            Map<String, Context> newContextMap = new HashMap<String, Context>();
            org.jackalope.study.conf.server.ServerConfiguration conf = null;
            while (iter.hasNext()) {
                String serverName = iter.next();
                Context serverContext = componentContextMap.get(serverName);
                if (serverContext != null) {
                    ServerType serverType = getKnownServer(serverContext.getString(BasicConfigurationConstants.CONFIG_TYPE));
                    boolean configSpecified = false;
                    String config = null;
                    if (serverType == null) {
                        config = serverContext.getString(BasicConfigurationConstants.CONFIG_CONFIG);
                        if (config == null || config.isEmpty()) {
                            config = "OTHER";
                        } else {
                            configSpecified = true;
                        }
                    } else {
                        config = serverType.toString().toUpperCase();
                        configSpecified = true;
                    }
                    try {
                        conf = (ServerConfiguration) ComponentConfigurationFactory.create(serverName, config, ComponentType.SERVER);
                        logger.debug("Created server " + serverName);
                        if (conf != null) {
                            conf.configure(serverContext);
                        }
                        if ((configSpecified && conf.isNotFoundConfigClass()) ||
                                !configSpecified) {
                            newContextMap.put(serverName, serverContext);
                        } else if (configSpecified) {
                            componentConfigMap.put(serverName, conf);
                        }
                        if (conf != null)
                            errorList.addAll(conf.getErrors());
                    } catch (ConfigurationException e) {
                        if (conf != null) errorList.addAll(conf.getErrors());
                        iter.remove();
                        logger.warn("Could not configure channel " + serverName
                                + " due to: " + e.getMessage(), e);
                    }
                } else {
                    iter.remove();
                    errorList.add(new ConfigurationError(this.agentName, serverName,
                            ConfigurationErrorType.CONFIG_ERROR, ErrorOrWarning.ERROR));
                }
            }
            componentContextMap = newContextMap;
            Set<String> tempServerSet = new HashSet<String>();
            tempServerSet.addAll(componentConfigMap.keySet());
            tempServerSet.addAll(componentContextMap.keySet());
            componentSet.retainAll(tempServerSet);
            return componentSet;
        }

        private ServerType getKnownServer(String type) {
            ServerType[] values = ServerType.values();
            for (ServerType value : values) {
                if (value.toString().equalsIgnoreCase(type))
                    return value;
                String channel = value.getServerClassName();
                if (channel != null && channel.equalsIgnoreCase(type))
                    return value;
            }
            return null;
        }

        protected String getSpaceDelimitedList(Set<String> entries) {
            if (entries.size() == 0) {
                return null;
            }

            StringBuilder sb = new StringBuilder("");

            for (String entry : entries) {
                sb.append(" ").append(entry);
            }

            return sb.toString().trim();
        }

    }
}
