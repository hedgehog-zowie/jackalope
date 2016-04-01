package org.jackalope.study.java8.lambda;

/**
 *
 * 更好的类型推测
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Speculate<T> {

    public static<T> T speculate(){
        return null;
    }

    public T speculate(T t){
        return t;
    }

    public static void main(String args[]){
        final String str = "string";
        System.out.println(String.valueOf(new Speculate<String>().speculate(str)));
        // 不可直接写成Speculate.speculate()，需要指定类型
        System.out.println(Speculate.<String>speculate());
        // 可以直接写成Speculate.speculate()
        System.out.println(String.valueOf(new Speculate<String>().speculate(Speculate.speculate())));
    }

}
