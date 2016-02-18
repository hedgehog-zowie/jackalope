package org.jackalope.study.designPattern.bridge;

/**
 *
 * 抽象角色
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Abstraction {

    private Implementation implementation;

    public Abstraction(Implementation implementation) {
        this.implementation = implementation;
    }

    public void service1(){
        implementation.impl1();
        implementation.impl2();
    }

    public void service2(){
        implementation.impl3();
        implementation.impl4();
    }

    public void service3(){
        implementation.impl5();
    }

    public Implementation getImplementation() {
        return implementation;
    }

    public void setImplementation(Implementation implementation) {
        this.implementation = implementation;
    }

}
