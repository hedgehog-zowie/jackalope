package org.jackalope.study.java8.lambda;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Java 8扩展了注解的上下文。现在几乎可以为任何东西添加注解：局部变量、泛型类、父类与接口的实现，就连方法的异常也能添加注解。
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class ExtendAnnotation {

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE_USE, ElementType.TYPE_PARAMETER})
    public @interface NonEmpty {
    }

    // 泛型、父类
    public static class Holder<@NonEmpty T> extends @NonEmpty Object {
        // 异常
        public void method() throws @NonEmpty Exception {
        }
    }

    @SuppressWarnings("unused")
    public static void main(String[] args) {
        // 局部变量
        final Holder<String> holder = new @NonEmpty Holder<>();
        // 局部变量、泛型
        @NonEmpty Collection<@NonEmpty String> strings = new ArrayList<>();
    }

}
