package org.jackalope.study.app;

import org.jackalope.study.conf.common.Context;
import org.jackalope.study.conf.component.ComponentConfiguration;
import org.jackalope.study.conf.exception.ConfigurationError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class IpAppConfiguration {

    public static class IpLibConfiguration {
        private final String agentName;

        private String iplibs;
        private String servers;

        private final Map<String, ComponentConfiguration> iplibsConfigMap;
        private final Map<String, ComponentConfiguration> serverConfigMap;

        private Map<String, Context> iplibContextMap;
        private Map<String, Context> serverContextMap;

        private Set<String> serverSet;
        private final List<ConfigurationError> errorList;

        private IpLibConfiguration(String serverName, List<ConfigurationError> errorList) {
            this.agentName = serverName;
            this.errorList = errorList;
            this.iplibsConfigMap = new HashMap<String, ComponentConfiguration>();
            this.serverConfigMap = new HashMap<String, ComponentConfiguration>();
            this.iplibContextMap = new HashMap<String, Context>();
            this.serverContextMap = new HashMap<String, Context>();
        }
    }

}
