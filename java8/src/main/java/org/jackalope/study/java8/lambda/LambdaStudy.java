package org.jackalope.study.java8.lambda;

import java.util.Arrays;
import java.util.List;

/**
 * 闭包
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class LambdaStudy {

    public static void main(String args[]) {

        List<String> list = Arrays.asList("c", "b", "a");
        list.forEach(e -> System.out.print(e));
        System.out.println("\n====");
        list.sort((e1, e2) -> e1.compareTo(e2));
        list.forEach(e -> System.out.print(e));
        System.out.println("\n====");

        print((Person person) -> person.getName().equals("person"));
        print2((Object o) -> o);
    }

    private static void print(CheckPerson checkPerson) {
        Person person = new Person();
        person.setName("person not");
        if (checkPerson.test(person))
            System.out.println("print checked person.");
        else
            System.out.println("print checked person not.");
    }

    private static void print2(MyFunctionInterface myFunctionInterface){
        System.out.println("print2");
    }

    private static class Person {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    interface CheckPerson {
        boolean test(Person person);
    }

    static class MyFunctionInterfaceClass<T> implements MyFunctionInterface<T> {

        @Override
        public T test(T t) {
            System.out.println(t);
            return t;
        }

    }

    @FunctionalInterface
    interface MyFunctionInterface<T> {
        T test(T t);
    }

}
