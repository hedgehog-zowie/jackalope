package org.jackalope.study.java8.lambda;

/**
 * 接口的默认方法
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class DefaultMethodAndStaticMethodOfInterface {

    public static void main(String args[]) {
        DefaultImpl di = new DefaultImpl();
        di.print();
        DefaultImpl2 di2 = new DefaultImpl2();
        di2.print();
        DefaultInterface defaultInterface = StaticInterface.createDefaultInterface();
        defaultInterface.test();
        defaultInterface.print();
    }

    // “默认方法”接口
    interface DefaultInterface {

        void test();

        // 默认方法
        default void print() {
            System.out.println("default print.");
        }

    }

    // “默认方法”实现类1
    static class DefaultImpl implements DefaultInterface{

        @Override
        public void test() {
            System.out.println("test");
        }

    }

    // “默认方法”实现类2
    static class DefaultImpl2 implements DefaultInterface {

        @Override
        public void test() {
            System.out.println("test");
        }

        // 覆盖默认方法2
        @Override
        public void print() {
            System.out.println("override print.");
        }

    }

    // “静态方法”接口
    interface StaticInterface {
        static DefaultInterface createDefaultInterface() {
            return DefaultImpl::new;
        }
    }

}

