package org.jackalope.study.app;

import org.jackalope.study.conf.client.ClientConfiguration;
import org.jackalope.study.conf.client.ClientType;
import org.jackalope.study.conf.common.AbstractAgentConfiguration;
import org.jackalope.study.conf.common.AbstractConfiguration;
import org.jackalope.study.conf.common.BasicConfigurationConstants;
import org.jackalope.study.conf.common.Context;
import org.jackalope.study.conf.component.ComponentConfigurationFactory;
import org.jackalope.study.conf.component.ComponentType;
import org.jackalope.study.conf.exception.ConfigurationError;
import org.jackalope.study.conf.exception.ConfigurationErrorType;
import org.jackalope.study.conf.exception.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class ClientAppConfiguration extends AbstractConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(ClientAppConfiguration.class);

    private final Map<String, AgentConfiguration> agentConfigMap;

    public ClientAppConfiguration(Map<String, String> properties) {
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

    @Override
    protected boolean addRawProperty(String name, String value) {
        if (name == null || value == null) {
            errors.add(new ConfigurationError("", "",
                    ConfigurationErrorType.AGENT_NAME_MISSING,
                    ConfigurationError.ErrorOrWarning.ERROR));
            return false;
        }

        if (value.trim().length() == 0) {
            errors.add(new ConfigurationError(name, "",
                    ConfigurationErrorType.PROPERTY_VALUE_NULL,
                    ConfigurationError.ErrorOrWarning.ERROR));
            return false;
        }

        name = name.trim();
        value = value.trim();

        int index = name.indexOf('.');

        if (index == -1) {
            errors.add(new ConfigurationError(name, "",
                    ConfigurationErrorType.AGENT_NAME_MISSING,
                    ConfigurationError.ErrorOrWarning.ERROR));
            return false;
        }

        String agentName = name.substring(0, index);

        if (agentName.length() == 0) {
            errors.add(new ConfigurationError(name, "",
                    ConfigurationErrorType.AGENT_NAME_MISSING,
                    ConfigurationError.ErrorOrWarning.ERROR));
            return false;
        }

        String configKey = name.substring(index + 1);

        if (configKey.length() == 0) {
            errors.add(new ConfigurationError(name, "",
                    ConfigurationErrorType.PROPERTY_NAME_NULL,
                    ConfigurationError.ErrorOrWarning.ERROR));
            return false;
        }

        AgentConfiguration aconf = agentConfigMap.get(agentName);

        if (aconf == null) {
            aconf = new AgentConfiguration(agentName, errors);
            agentConfigMap.put(agentName, aconf);
        }

        return aconf.addProperty(configKey, value);
    }

    @Override
    protected void validateConfiguration() {
        Iterator<String> it = agentConfigMap.keySet().iterator();

        while (it.hasNext()) {
            String agentName = it.next();
            AgentConfiguration aconf = agentConfigMap.get(agentName);

            if (!aconf.isValid()) {
                logger.warn("Agent configuration invalid for agent '" + agentName + "'. It will be removed.");
                errors.add(new ConfigurationError(agentName, "",
                        ConfigurationErrorType.AGENT_CONFIGURATION_INVALID,
                        ConfigurationError.ErrorOrWarning.ERROR));
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
            if (key.equals(BasicConfigurationConstants.CONFIG_CLIENTS)) {
                if (components == null) {
                    components = value;
                    return true;
                } else {
                    logger.warn("Duplicate iplib list specified for agent: " + agentName);
                    errorList.add(new ConfigurationError(agentName,
                            BasicConfigurationConstants.CONFIG_CLIENTS,
                            ConfigurationErrorType.DUPLICATE_PROPERTY,
                            ConfigurationError.ErrorOrWarning.ERROR));
                    return false;
                }
            }

            ComponentNameAndConfigKey cnck = parseConfigKey(key, BasicConfigurationConstants.CONFIG_CLIENTS_PREFIX);

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
            errorList.add(new ConfigurationError(agentName, key, ConfigurationErrorType.INVALID_PROPERTY, ConfigurationError.ErrorOrWarning.ERROR));
            return false;
        }

        @Override
        protected boolean isValid() {
            logger.debug("Starting validation of configuration for agent: "
                    + agentName + ", initial-configuration: " + this.getPrevalidationConfig());

            if (components == null || components.trim().length() == 0) {
                logger.warn("Agent configuration for '" + agentName
                        + "' does not contain any components. Marking it as invalid.");
                errorList.add(new ConfigurationError(agentName,
                        BasicConfigurationConstants.CONFIG_CLIENTS,
                        ConfigurationErrorType.PROPERTY_VALUE_NULL,
                        ConfigurationError.ErrorOrWarning.ERROR));
                return false;
            }

            componentSet = new HashSet<String>(Arrays.asList(components.split("\\s+")));

            componentSet = validateConponents(componentSet);
            if (componentSet.size() == 0) {
                logger.warn("Agent configuration for '" + agentName
                        + "' does not contain any valid components. Marking it as invalid.");
                errorList.add(new ConfigurationError(agentName,
                        BasicConfigurationConstants.CONFIG_CLIENTS,
                        ConfigurationErrorType.PROPERTY_VALUE_NULL,
                        ConfigurationError.ErrorOrWarning.ERROR));
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
            org.jackalope.study.conf.client.ClientConfiguration conf = null;
            while (iter.hasNext()) {
                String componentName = iter.next();
                Context componentContext = componentContextMap.get(componentName);
                if (componentContext != null) {
                    ClientType clientType = getKnownClient(componentContext.getString(BasicConfigurationConstants.CONFIG_TYPE));
                    boolean configSpecified = false;
                    String config = null;
                    if (clientType == null) {
                        config = componentContext.getString(BasicConfigurationConstants.CONFIG_CONFIG);
                        if (config == null || config.isEmpty()) {
                            config = "OTHER";
                        } else {
                            configSpecified = true;
                        }
                    } else {
                        config = clientType.toString().toUpperCase();
                        configSpecified = true;
                    }
                    try {
                        conf = (ClientConfiguration) ComponentConfigurationFactory.create(componentName, config, ComponentType.CLIENT);
                        logger.debug("Created server " + componentName);
                        if (conf != null) {
                            conf.configure(componentContext);
                        }
                        if ((configSpecified && conf.isNotFoundConfigClass()) || !configSpecified) {
                            newContextMap.put(componentName, componentContext);
                        } else if (configSpecified) {
                            componentConfigMap.put(componentName, conf);
                        }
                        if (conf != null)
                            errorList.addAll(conf.getErrors());
                    } catch (ConfigurationException e) {
                        if (conf != null) errorList.addAll(conf.getErrors());
                        iter.remove();
                        logger.warn("Could not configure channel " + componentName + " due to: " + e.getMessage(), e);
                    }
                } else {
                    iter.remove();
                    errorList.add(new ConfigurationError(this.agentName, componentName,
                            ConfigurationErrorType.CONFIG_ERROR, ConfigurationError.ErrorOrWarning.ERROR));
                }
            }
            componentContextMap = newContextMap;
            Set<String> tempServerSet = new HashSet<String>();
            tempServerSet.addAll(componentConfigMap.keySet());
            tempServerSet.addAll(componentContextMap.keySet());
            componentSet.retainAll(tempServerSet);
            return componentSet;
        }

        private ClientType getKnownClient(String type) {
            ClientType[] values = ClientType.values();
            for (ClientType value : values) {
                if (value.toString().equalsIgnoreCase(type))
                    return value;
                String channel = value.getClientClassName();
                if (channel != null && channel.equalsIgnoreCase(type))
                    return value;
            }
            return null;
        }

    }

    /* Getters */
    public Map<String, AgentConfiguration> getAgentConfigMap() {
        return agentConfigMap;
    }

}
