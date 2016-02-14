package org.jackalope.study.designPattern.factory.factoryMethod;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class IPhone implements MobilePhone {

    protected IPhone(){}

    @Override
    public void showInfo() {
        System.out.println("I'm iPhone.");
    }

}
