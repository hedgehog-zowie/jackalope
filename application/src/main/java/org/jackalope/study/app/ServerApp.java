package org.jackalope.study.app;

import com.iuni.data.avro.server.Server;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class ServerApp {
    private static final Logger logger = LoggerFactory.getLogger(ServerApp.class);

    public static void main(String[] args) {

        try {

            File configurationFile = new File(ClientApp.class.getResource("/server.properties").getPath());

            if (!configurationFile.exists()) {
                String path = configurationFile.getPath();
                try {
                    path = configurationFile.getCanonicalPath();
                } catch (IOException ex) {
                    logger.error("Failed to read canonical path for file: " + path, ex);
                }
                throw new ParseException("The specified configuration file does not exist: " + path);
            }
            ServerAppConfigurationProvider configurationProvider = new ServerAppConfigurationProvider("data", configurationFile);
            ServerAppConfiguration.AgentConfiguration agentConfiguration = configurationProvider.getConfiguration().getAgentConfigMap().get("data");

            Map<String, Server> clientMap = configurationProvider.loadServers(agentConfiguration);
            for(Server server : clientMap.values()){
                server.start();
            }
        } catch (Exception e) {
            logger.error("A fatal error occurred while running. Exception follows.", e);
        }

    }
}
