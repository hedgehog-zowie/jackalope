package org.jackalope.study.app;

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

public class ServerAppConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(ServerAppConfiguration.class);

    private final Map<String, AgentConfiguration> appConfigMap;
    private final LinkedList<ConfigurationError> errors;
    public static final String NEWLINE = System.getProperty("line.separator", "\n");
    public static final String INDENTSTEP = "  ";

    public ServerAppConfiguration(Map<String, String> properties) {
        appConfigMap = new HashMap<String, AgentConfiguration>();
        errors = new LinkedList<ConfigurationError>();
        for (String name : properties.keySet()) {
            String value = properties.get(name);
            if (!addRawProperty(name, value)) {
                logger.warn("Configuration property ignored: " + name + " = " + value);
            }
        }
        validateConfiguration();
    }

    public List<ConfigurationError> getConfigurationErrors() {
        return errors;
    }

    public AgentConfiguration getConfigurationFor(String hostname) {
        return appConfigMap.get(hostname);
    }

    private void validateConfiguration() {
        Iterator<String> it = appConfigMap.keySet().iterator();

        while (it.hasNext()) {
            String appName = it.next();
            AgentConfiguration aconf = appConfigMap.get(appName);

            if (!aconf.isValid()) {
                logger.warn("App configuration invalid for app '" + appName + "'. It will be removed.");
                errors.add(new ConfigurationError(appName, "",
                        ConfigurationErrorType.AGENT_CONFIGURATION_INVALID,
                        ErrorOrWarning.ERROR));
                it.remove();
            }
            logger.debug("Channels:" + aconf.servers + "\n");
        }

        logger.info("Post-validation configuration contains configuration"
                + " for agents: " + appConfigMap.keySet());
    }

    private boolean addRawProperty(String name, String value) {
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

        AgentConfiguration sconf = appConfigMap.get(agentName);

        if (sconf == null) {
            sconf = new AgentConfiguration(agentName, errors);
            appConfigMap.put(agentName, sconf);
        }

        return sconf.addProperty(configKey, value);
    }

    public static class AgentConfiguration {
        private final String agentName;

        private String servers;

        private final Map<String, ComponentConfiguration> serverConfigMap;

        private Map<String, Context> serverContextMap;

        private Set<String> serverSet;

        private final List<ConfigurationError> errorList;

        private AgentConfiguration(String agentName, List<ConfigurationError> errorList) {
            this.agentName = agentName;
            this.errorList = errorList;
            this.serverConfigMap = new HashMap<String, ComponentConfiguration>();
            this.serverContextMap = new HashMap<String, Context>();
        }

        public String getAgentName() {
            return agentName;
        }

        public Map<String, ComponentConfiguration> getServerConfigMap() {
            return serverConfigMap;
        }

        public Map<String, Context> getServerContextMap() {
            return serverContextMap;
        }

        public void setServerContextMap(Map<String, Context> serverContextMap) {
            this.serverContextMap = serverContextMap;
        }

        public Set<String> getServerSet() {
            return serverSet;
        }

        public void setServerSet(Set<String> serverSet) {
            this.serverSet = serverSet;
        }

        public List<ConfigurationError> getErrorList() {
            return errorList;
        }

        private boolean isValid() {
            logger.debug("Starting validation of configuration for agent: "
                    + agentName + ", initial-configuration: " + this.getPrevalidationConfig());

            // Make sure that at least one channel is specified
            if (servers == null || servers.trim().length() == 0) {
                logger.warn("Agent configuration for '" + agentName
                        + "' does not contain any channels. Marking it as invalid.");
                errorList.add(new ConfigurationError(agentName,
                        BasicConfigurationConstants.CONFIG_SERVERS,
                        ConfigurationErrorType.PROPERTY_VALUE_NULL,
                        ErrorOrWarning.ERROR));
                return false;
            }

            serverSet = new HashSet<String>(Arrays.asList(servers.split("\\s+")));

            serverSet = validateServers(serverSet);
            if (serverSet.size() == 0) {
                logger.warn("Agent configuration for '" + agentName
                        + "' does not contain any valid channels. Marking it as invalid.");
                errorList.add(new ConfigurationError(agentName,
                        BasicConfigurationConstants.CONFIG_SERVERS,
                        ConfigurationErrorType.PROPERTY_VALUE_NULL,
                        ErrorOrWarning.ERROR));
                return false;
            }

            this.servers = getSpaceDelimitedList(serverSet);

            logger.debug("Post validation configuration for " + agentName + NEWLINE
                    + this.getPostvalidationConfig());

            return true;
        }

        private Set<String> validateServers(Set<String> serverSet) {
            Iterator<String> iter = serverSet.iterator();
            Map<String, Context> newContextMap = new HashMap<String, Context>();
            org.jackalope.study.conf.server.ServerConfiguration conf = null;
            while (iter.hasNext()) {
                String serverName = iter.next();
                Context serverContext = serverContextMap.get(serverName);
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
                            serverConfigMap.put(serverName, conf);
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
            serverContextMap = newContextMap;
            Set<String> tempServerSet = new HashSet<String>();
            tempServerSet.addAll(serverConfigMap.keySet());
            tempServerSet.addAll(serverContextMap.keySet());
            serverSet.retainAll(tempServerSet);
            return serverSet;
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

        private static Set<String> stringToSet(String target, String delim) {
            Set<String> out = new HashSet<String>();
            if (target == null || target.trim().length() == 0) {
                return out;
            }
            StringTokenizer t = new StringTokenizer(target, delim);
            while (t.hasMoreTokens()) {
                out.add(t.nextToken());
            }
            return out;
        }

        public String getPrevalidationConfig() {
            StringBuilder sb = new StringBuilder("AgentConfiguration[");
            sb.append(agentName).append("]").append(NEWLINE).append("SOURCES: ");
            sb.append(serverContextMap).append(NEWLINE).append("CHANNELS: ");

            return sb.toString();
        }

        public String getPostvalidationConfig() {
            StringBuilder sb = new StringBuilder(
                    "AgentConfiguration created without Configuration stubs " +
                            "for which only basic syntactical validation was performed[");
            sb.append(agentName).append("]").append(NEWLINE);
            if (!serverContextMap.isEmpty()) {
                if (!serverContextMap.isEmpty()) {
                    sb.append("SOURCES: ").append(serverContextMap).append(NEWLINE);
                }
            }

            if (!serverConfigMap.isEmpty()) {
                sb.append("AgentConfiguration created with Configuration stubs " +
                        "for which full validation was performed[");
                sb.append(agentName).append("]").append(NEWLINE);

                if (!serverConfigMap.isEmpty()) {
                    sb.append("SOURCES: ").append(serverConfigMap).append(NEWLINE);
                }
            }

            return sb.toString();
        }

        private boolean addProperty(String key, String value) {
            // Check for iplib
            if (key.equals(BasicConfigurationConstants.CONFIG_SERVERS)) {
                if (servers == null) {
                    servers = value;
                    return true;
                } else {
                    logger.warn("Duplicate iplib list specified for agent: " + agentName);
                    errorList.add(new ConfigurationError(agentName,
                            BasicConfigurationConstants.CONFIG_IPLIBS,
                            ConfigurationErrorType.DUPLICATE_PROPERTY,
                            ErrorOrWarning.ERROR));
                    return false;
                }
            }

            ComponentNameAndConfigKey cnck = parseConfigKey(key, BasicConfigurationConstants.CONFIG_SERVERS_PREFIX);

            if (cnck != null) {
                // it is a source
                String name = cnck.getComponentName();
                Context srcConf = serverContextMap.get(name);

                if (srcConf == null) {
                    srcConf = new Context();
                    serverContextMap.put(name, srcConf);
                }

                srcConf.put(cnck.getConfigKey(), value);
                return true;
            }

            logger.warn("Invalid property specified: " + key);
            errorList.add(new ConfigurationError(agentName, key, ConfigurationErrorType.INVALID_PROPERTY, ErrorOrWarning.ERROR));
            return false;
        }

        private ComponentNameAndConfigKey parseConfigKey(String key, String prefix) {
            // key must start with prefix
            if (!key.startsWith(prefix)) {
                return null;
            }

            // key must have a component name part after the prefix of the format:
            // <prefix><component-name>.<conf-key>
            int index = key.indexOf('.', prefix.length() + 1);

            if (index == -1) {
                return null;
            }

            String name = key.substring(prefix.length(), index);
            String configKey = key.substring(prefix.length() + name.length() + 1);

            // name and conf key must be non-empty
            if (name.length() == 0 || configKey.length() == 0) {
                return null;
            }

            return new ComponentNameAndConfigKey(name, configKey);
        }
    }

    public static class ComponentNameAndConfigKey {

        private final String componentName;
        private final String configKey;

        private ComponentNameAndConfigKey(String name, String configKey) {
            this.componentName = name;
            this.configKey = configKey;
        }

        public String getComponentName() {
            return componentName;
        }

        public String getConfigKey() {
            return configKey;
        }
    }
}
