package org.jackalope.study.hbase;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public class IuniRowKeyGenerator {
    public static AtomicLong atl = new AtomicLong();
    public static byte[] getReverseTimestampKey(String timestamp) {
        return new StringBuilder(UUID.randomUUID().toString())
                .append("-").append(timestamp)
                .toString().getBytes();
    }
}
