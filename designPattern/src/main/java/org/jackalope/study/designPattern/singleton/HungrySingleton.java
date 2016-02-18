package org.jackalope.study.designPattern.singleton;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class HungrySingleton {

    /**
     * 饿汉式在类加载时创建实例
     */
    private static final HungrySingleton singletonInstance = new HungrySingleton();

    private HungrySingleton(){}

    public HungrySingleton getSingletonInstance(){
        return singletonInstance;
    }

}
