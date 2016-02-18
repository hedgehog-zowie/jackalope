package org.jackalope.study.designPattern.singleton;

/**
 *
 * effective java建议的方式
 * 1.避免线程同步的问题；
 * 2.避免序列化重新创建新对象的问题；
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public enum EnumSingleton {

    INSTANCE;

}
