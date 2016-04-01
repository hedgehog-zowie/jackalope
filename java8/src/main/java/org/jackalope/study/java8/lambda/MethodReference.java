package org.jackalope.study.java8.lambda;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 *
 * 方法引用
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class MethodReference {

    public static void main(String args[]) {
        // 构造器引用
        TestClass testClass = TestClass.create(TestClass::new);
        List<TestClass> list = Arrays.asList(testClass);
        // 静态方法引用，有参数
        list.forEach(TestClass::print);
        // 普通方法引用，无参数
        list.forEach(TestClass::print2);
        // 普通方法引用，有参数
        list.forEach(testClass::print2);
    }

    static class TestClass {

        public static TestClass create(Supplier<TestClass> supplier) {
            return supplier.get();
        }

        public static void print(final TestClass testClass) {
            System.out.println(testClass.toString());
        }

        public void print2(final TestClass testClass) {
            System.out.println(testClass.toString());
        }

        public void print2() {
            System.out.println(this.toString());
        }

    }

}
