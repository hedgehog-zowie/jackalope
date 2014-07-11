package org.jackalope.study.conf.common;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public final class BasicConfigurationConstants {

    public static final String CONFIG_CLIENTS = "clients";
    public static final String CONFIG_CLIENTS_PREFIX = CONFIG_CLIENTS + ".";

    public static final String CONFIG_SERVERS = "servers.";
    public static final String CONFIG_SERVERS_PREFIX = CONFIG_SERVERS + ".";

    public static final String CONFIG_IPLIBS = "iplibs";
    public static final String CONFIG_IPLIBS_PREFIX = CONFIG_IPLIBS + ".";

    public static final String CONFIG_CONFIG = "conf";
    public static final String CONFIG_TYPE = "type";

    // 禁止创建实例
    private BasicConfigurationConstants() {
    }

}
