package org.jackalope.study.conf.iplib;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public enum IplibType {
    OTHER(null),
    IUNI("org.jackalope.study.iplib.IpUtils"),
    TAOBAO("org.jackalope.study.iplib.IpUtils"),
    PURE("org.jackalope.study.iplib.IpUtils");

    private final String ipLibClassName;

    private IplibType(String ipLibClassName) {
        this.ipLibClassName = ipLibClassName;
    }

    public String getIpLibClassName() {
        return ipLibClassName;
    }

}
