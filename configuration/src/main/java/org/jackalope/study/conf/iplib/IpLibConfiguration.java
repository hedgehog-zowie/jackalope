package org.jackalope.study.conf.iplib;

import org.jackalope.study.conf.component.ComponentConfiguration;

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

        /**
         * iuni iplib
         *
         * @see org.jackalope.study.iplib.IuniIpLib
         */
        IUNI("org.jackalope.study.conf.iplib.IuniIpLibConfiguration"),
        TAOBAO("org.jackalope.study.conf.iplib.TaobaoIpLibConfiguration"),
        PURE("org.jackalope.study.conf.iplib.PureIpLibConfiguration");
        private String configurationName;

        private IpLibConfigurationType(String configurationName) {
            this.configurationName = configurationName;
        }

        public String getConfigurationName() {
            return this.configurationName;
        }


    }

}
