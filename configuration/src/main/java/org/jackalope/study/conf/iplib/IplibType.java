package org.jackalope.study.conf.iplib;

import org.jackalope.study.conf.exception.ConfigurationException;
import org.jackalope.study.conf.component.ComponentConfiguration;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public enum IplibType {
    /**
     * other ip lib
     */
    OTHER(null),

    /**
     * mine ip lib
     *
     */
    IUNI("org.jackalope.study.iplib.IpUtils"),

    /**
     * taobao ip lib
     *
     */
    TAOBAO("org.jackalope.study.iplib.IpUtils"),

    /**
     * pure ip lib
     *
     */
    PURE("org.jackalope.study.iplib.IpUtils");

    private final String ipLibClassName;

    private IplibType(String ipLibClassName) {
        this.ipLibClassName = ipLibClassName;
    }

    public String getIpLibClassName() {
        return ipLibClassName;
    }

    public ComponentConfiguration getConfiguration(String name) throws ConfigurationException {
        // TODO get configuration of iplib
        return null;
    }
}
