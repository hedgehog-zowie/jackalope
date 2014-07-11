package org.jackalope.study.conf.iplib;

import org.jackalope.study.conf.component.ComponentConfiguration;
import org.jackalope.study.conf.exception.ConfigurationException;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class IpLibConfiguration extends ComponentConfiguration {

    public IpLibConfiguration(String componentName) {
        super(componentName);
    }

    public enum IpLibConfigurationType {
        OTHER(null),

        IUNI("org.jackalope.study.conf.iplib.IuniIpLibConfiguration"),
        TAOBAO("org.jackalope.study.conf.iplib.TaobaoIpLibConfiguration"),
        PURE("org.jackalope.study.conf.iplib.PureIpLibConfiguration");
        private String iplibConfigurationName;

        private IpLibConfigurationType(String iplib) {
            this.iplibConfigurationName = iplib;
        }

        public String getIplibConfigurationName() {
            return this.iplibConfigurationName;
        }

        public IpLibConfiguration getConfiguration(String name) throws ConfigurationException {
            if (this.equals(IpLibConfigurationType.OTHER)) {
                return new IpLibConfiguration(name);
            }
            Class<? extends IpLibConfiguration> clazz = null;
            IpLibConfiguration instance = null;
            try {
                if (iplibConfigurationName != null) {
                    clazz = (Class<? extends IpLibConfiguration>) Class.forName(iplibConfigurationName);
                    instance = clazz.getConstructor(String.class).newInstance(name);
                } else {
                    instance = new IpLibConfiguration(name);
                    instance.setNotFoundConfigClass();
                }
            } catch (ClassNotFoundException e) {
                instance = new IpLibConfiguration(name);
                instance.setNotFoundConfigClass();
            } catch (Exception e) {
                throw new ConfigurationException("Error creating configuration", e);
            }
            return instance;
        }
    }

}
