package org.jackalope.study.designPattern.build;

import java.util.List;

/**
 *
 * 指导者角色
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Director {

    private Builder builder;

    public Director(Builder builder){
        this.builder = builder;
    }

    public MobilePhone getPhone(List<String> components){
        return builder.buildPhone(components);
    }

}
