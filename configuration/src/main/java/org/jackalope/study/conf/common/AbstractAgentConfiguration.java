package org.jackalope.study.conf.common;

import org.jackalope.study.conf.component.ComponentConfiguration;
import org.jackalope.study.conf.exception.ConfigurationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public abstract class AbstractAgentConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(AbstractAgentConfiguration.class);

    protected final String agentName;

    protected String components;

    protected final Map<String, ComponentConfiguration> componentConfigMap;

    protected Map<String, Context> componentContextMap;

    protected Set<String> componentSet;

    protected final List<ConfigurationError> errorList;

    protected AbstractAgentConfiguration(String agentName, List<ConfigurationError> errorList) {
        this.agentName = agentName;
        this.errorList = errorList;
        this.componentConfigMap = new HashMap<String, ComponentConfiguration>();
        this.componentContextMap = new HashMap<String, Context>();
    }

    public String getAgentName() {
        return agentName;
    }

    public String getComponents() {
        return components;
    }

    public Map<String, ComponentConfiguration> getComponentConfigMap() {
        return componentConfigMap;
    }

    public Map<String, Context> getComponentContextMap() {
        return componentContextMap;
    }

    public void setComponentContextMap(Map<String, Context> componentContextMap) {
        this.componentContextMap = componentContextMap;
    }

    public Set<String> getComponentSet() {
        return componentSet;
    }

    public void setComponentSet(Set<String> componentSet) {
        this.componentSet = componentSet;
    }

    public List<ConfigurationError> getErrorList() {
        return errorList;
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

    protected Set<String> stringToSet(String target, String delim) {
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
        StringBuilder sb = new StringBuilder("AgentConfiguration[")
                .append(agentName).append("]")
                .append("COMPONENTS: ").append(componentContextMap);

        return sb.toString();
    }

    public String getPostvalidationConfig() {
        StringBuilder sb = new StringBuilder("AgentConfiguration created without Configuration stubs " +
                "for which only basic syntactical validation was performed[");
        sb.append(agentName).append("]");
        if (!componentContextMap.isEmpty()) {
            if (!componentContextMap.isEmpty()) {
                sb.append("COMPONENTS: ").append(componentContextMap);
            }
        }

        if (!componentConfigMap.isEmpty()) {
            sb.append("AgentConfiguration created with Configuration stubs " +
                    "for which full validation was performed[");
            sb.append(agentName).append("]");

            if (!componentConfigMap.isEmpty()) {
                sb.append("COMPONENTS: ").append(componentConfigMap);
            }
        }

        return sb.toString();
    }

    protected ComponentNameAndConfigKey parseConfigKey(String key, String prefix) {
        if (!key.startsWith(prefix)) {
            return null;
        }

        int index = key.indexOf('.', prefix.length() + 1);

        if (index == -1) {
            return null;
        }

        String name = key.substring(prefix.length(), index);
        String configKey = key.substring(prefix.length() + name.length() + 1);

        if (name.length() == 0 || configKey.length() == 0) {
            return null;
        }

        return new ComponentNameAndConfigKey(name, configKey);
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

    protected abstract boolean addProperty(String key, String value);

    protected abstract boolean isValid();

    protected abstract Set<String> validateConponents(Set<String> componentSet);
}
