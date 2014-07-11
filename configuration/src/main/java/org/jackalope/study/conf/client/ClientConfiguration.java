package org.jackalope.study.conf.client;

import org.jackalope.study.conf.common.Context;
import org.jackalope.study.conf.component.ComponentConfiguration;
import org.jackalope.study.conf.exception.ConfigurationException;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class ClientConfiguration extends ComponentConfiguration {
    public ClientConfiguration(String componentName) {
        super(componentName);
    }

    @Override
    public void configure(Context context) throws ConfigurationException {
        super.configure(context);
    }

    public enum ClientConfigurationType {
        OTHER(null),
        HTTP("org.jackalope.study.conf.client.HttpClientConfiguration"),
        NETTY("org.jackalope.study.conf.client.NettyClientConfiguration");

        private String clientConfigurationName;

        private ClientConfigurationType(String client) {
            clientConfigurationName = client;
        }

        public String getClientConfigurationName() {
            return this.clientConfigurationName;
        }

        public ClientConfiguration getConfiguration(String name) throws ConfigurationException {
            if (this.equals(ClientConfigurationType.OTHER)) {
                return new ClientConfiguration(name);
            }
            Class<? extends ClientConfiguration> clazz = null;
            ClientConfiguration instance = null;
            try {
                if (clientConfigurationName != null) {
                    clazz = (Class<? extends ClientConfiguration>) Class.forName(clientConfigurationName);
                    instance = clazz.getConstructor(String.class).newInstance(name);
                } else {
                    instance = new ClientConfiguration(name);
                    instance.setNotFoundConfigClass();
                }
            } catch (ClassNotFoundException e) {
                instance = new ClientConfiguration(name);
                instance.setNotFoundConfigClass();
            } catch (Exception e) {
                throw new ConfigurationException("Error creating configuration", e);
            }
            return instance;
        }
    }
}
