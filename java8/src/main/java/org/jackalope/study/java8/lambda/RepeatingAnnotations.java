package org.jackalope.study.java8.lambda;

import java.lang.annotation.*;

/**
 * 重复注解
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class RepeatingAnnotations {

    // @target这个注解来指定给哪一类java成员注解，指定注解目标该是什么样的东西
    @Target(ElementType.TYPE)
    // @Retention 表示注解运行的状态，换句话说，注解改在什么样的状态下才能运行
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Filters {
        Filter[] value();
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    // 重复注解，若不添加，在使用重复注解时（如两个@Filter）会报错：does not have a valid java.lang.annotation.Repeatable annotation
    @Repeatable(Filters.class)
    public @interface Filter {
        String value();
    }

    @Filter("filter1")
    @Filter("filter2")
    public interface Filterable {
    }

    public static void main(String[] args) {
        for( Filter filter: Filterable.class.getAnnotationsByType( Filter.class ) ) {
            System.out.println( filter.value() );
        }
    }

}
