package org.jackalope.study.designPattern.bridge;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class ClientService2 extends Abstraction {
    public ClientService2(Implementation implementation) {
        super(implementation);
    }

    /**
     * 直接调用实现角色
     */
    public void serviceC(){
        getImplementation().impl3();
    }
}
