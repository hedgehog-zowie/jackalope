package org.jackalope.study.designPattern.bridge;

/**
 *
 * 具体抽象角色
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class ClientService1 extends Abstraction {

    public ClientService1(Implementation implementation) {
        super(implementation);
    }

    public void serviceA(){
        service1();
        service3();
    }

    public void serviceB(){
        service2();
    }

}
