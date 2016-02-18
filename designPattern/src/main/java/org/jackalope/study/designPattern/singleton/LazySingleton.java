package org.jackalope.study.designPattern.singleton;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class LazySingleton {

    private static LazySingleton singletonInstance = null;

    private LazySingleton() {
    }

    /**
     * 懒汉式在使用时才检查/创建实例
     * 注意加synchronized以同步
     * @return
     */
    public synchronized static LazySingleton getSingletonInstance() {
        if (singletonInstance == null)
            singletonInstance = new LazySingleton();
        return singletonInstance;
    }

}
