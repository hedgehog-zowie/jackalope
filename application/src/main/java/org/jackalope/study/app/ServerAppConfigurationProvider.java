package org.jackalope.study.app;

import com.google.common.collect.Maps;
import com.iuni.data.avro.DefaultServerFactory;
import com.iuni.data.avro.ServerFactory;
import com.iuni.data.avro.exceptions.RpcServerException;
import com.iuni.data.avro.server.Server;
import org.jackalope.study.conf.common.AbstractConfigurationProvider;
import org.jackalope.study.conf.common.BasicConfigurationConstants;
import org.jackalope.study.conf.common.ConfigUtils;
import org.jackalope.study.conf.common.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class ServerAppConfigurationProvider extends AbstractConfigurationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerAppConfigurationProvider.class);

    private final ServerFactory serverFactory;
    private final Map<Class<? extends Server>, Map<String, Server>> serverCache;

    public ServerAppConfigurationProvider(String agentName, File file){
        super(agentName, file);
        serverFactory = new DefaultServerFactory();
        serverCache = new HashMap<Class<? extends Server>, Map<String, Server>>();
    }

    @Override
    public ServerAppConfiguration getConfiguration() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            Properties properties = new Properties();
            properties.load(reader);
            return new ServerAppConfiguration(toMap(properties));
        } catch (IOException ex) {
            LOGGER.error("Unable to load file:" + file + " (I/O failure) - Exception follows.", ex);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    LOGGER.warn("Unable to close file reader for file: " + file, ex);
                }
            }
        }
        return new ServerAppConfiguration(new HashMap<String, String>());
    }

    public Map<String, Server> loadServers(ServerAppConfiguration.AgentConfiguration agentConf) throws RpcServerException {
        Map<String, Server> serverMap = Maps.newHashMap();

        Set<String> serverNames = agentConf.getComponentSet();

        for (String name : serverNames) {
            Context context = agentConf.getComponentContextMap().get(name);
            if (context != null) {
                Server server = getOrCreateServer(name, context.getString(BasicConfigurationConstants.CONFIG_TYPE));
                try {
                    ConfigUtils.configure(server, context);
                    serverMap.put(name, server);
                    LOGGER.info("Created server " + name);
                } catch (Exception e) {
                    String msg = String.format("Server %s has been removed due to an error during configuration", name);
                    LOGGER.error(msg, e);
                }
            }
        }
        return serverMap;
    }

    private Server getOrCreateServer(String name, String type) throws RpcServerException {
        Class<? extends Server> serverClass = serverFactory.getClass(type);
        Map<String, Server> serverMap = serverCache.get(serverClass);
        if (serverMap == null) {
            serverMap = Maps.newHashMap();
            serverCache.put(serverClass, serverMap);
        }
        Server client = serverMap.get(name);
        if (client == null) {
            client = serverFactory.create(name, type);
            client.setName(name);
            serverMap.put(name, client);
        }
        return client;
    }

}
