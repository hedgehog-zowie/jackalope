package org.jackalope.study.app;

import com.google.common.collect.Maps;
import com.iuni.data.avro.ClientFactory;
import com.iuni.data.avro.DefaultClientFactory;
import com.iuni.data.avro.client.Client;
import com.iuni.data.avro.exceptions.RpcClientException;
import org.jackalope.study.conf.common.AbstractConfigurationProvider;
import org.jackalope.study.conf.common.BasicConfigurationConstants;
import org.jackalope.study.conf.common.ConfigUtils;
import org.jackalope.study.conf.common.Context;
import org.jackalope.study.conf.component.ComponentConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class ClientAppConfigurationProvider extends AbstractConfigurationProvider{
    private static final Logger LOGGER = LoggerFactory.getLogger(ClientAppConfigurationProvider.class);

    private final ClientFactory clientFactory;
    private final Map<Class<? extends Client>, Map<String, Client>> clientCache;

    public ClientAppConfigurationProvider(String agentName, File file){
        super(agentName, file);
        clientFactory = new DefaultClientFactory();
        clientCache = new HashMap<Class<? extends Client>, Map<String, Client>>();
    }

    @Override
    public ClientAppConfiguration getConfiguration() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            Properties properties = new Properties();
            properties.load(reader);
            return new ClientAppConfiguration(toMap(properties));
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
        return new ClientAppConfiguration(new HashMap<String, String>());
    }

    public Map<String, Client> loadClients(ClientAppConfiguration.AgentConfiguration agentConf) throws RpcClientException {
        Map<String, Client> clientMap = Maps.newHashMap();

        Set<String> clientNames = agentConf.getComponentSet();

        for (String name : clientNames) {
            Context context = agentConf.getComponentContextMap().get(name);
            if (context != null) {
                Client client = getOrCreateClient(name, context.getString(BasicConfigurationConstants.CONFIG_TYPE));
                try {
                    ConfigUtils.configure(client, context);
                    clientMap.put(name, client);
                    LOGGER.info("Created client " + name);
                } catch (Exception e) {
                    String msg = String.format("Client %s has been removed due to an " +
                            "error during configuration", name);
                    LOGGER.error(msg, e);
                }
            }
        }
        return clientMap;
    }

    private Client getOrCreateClient(String name, String type) throws RpcClientException {
        Class<? extends Client> clientClass = clientFactory.getClass(type);
        Map<String, Client> clientMap = clientCache.get(clientClass);
        if (clientMap == null) {
            clientMap = Maps.newHashMap();
            clientCache.put(clientClass, clientMap);
        }
        Client client = clientMap.get(name);
        if (client == null) {
            client = clientFactory.create(name, type);
            client.setName(name);
            clientMap.put(name, client);
        }
        return client;
    }
}
