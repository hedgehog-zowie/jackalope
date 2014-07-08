package org.jackalope.study.conf.component;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public enum ComponentType {
    OTHER(null),

    RPC("Rpc"),
    IPLIB("IpLib");

    private final String componentType;

    private ComponentType(String type) {
        componentType = type;
    }

    public String getComponentType() {
        return componentType;
    }
}