package org.jackalope.study.app;

import com.iuni.data.avro.client.Client;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class ClientApp {

    private static final Logger logger = LoggerFactory.getLogger(ClientApp.class);

    public static final String CONF_MONITOR_CLASS = "flume.monitoring.type";
    public static final String CONF_MONITOR_PREFIX = "flume.monitoring.";

    private static final String cname = "c1";

    public static void main(String[] args) {

        try {

            File configurationFile = new File(ClientApp.class.getResource("/client.properties").getPath());

            if (!configurationFile.exists()) {
                String path = configurationFile.getPath();
                try {
                    path = configurationFile.getCanonicalPath();
                } catch (IOException ex) {
                    logger.error("Failed to read canonical path for file: " + path, ex);
                }
                throw new ParseException("The specified configuration file does not exist: " + path);
            }
            ClientAppConfigurationProvider configurationProvider = new ClientAppConfigurationProvider(cname, configurationFile);
            ClientAppConfiguration.AgentConfiguration agentConfiguration = configurationProvider.getConfiguration().getAgentConfigMap().get("data");

            Map<String, Client> clientMap = configurationProvider.loadClients(agentConfiguration);
            for(Client client : clientMap.values()){
                client.getData();
            }
        } catch (Exception e) {
            logger.error("A fatal error occurred while running. Exception follows.", e);
        }
    }

}