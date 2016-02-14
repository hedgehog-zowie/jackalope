package org.jackalope.study.designPattern.factory.factoryMethod;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class HuaweiPhone implements MobilePhone {

    protected HuaweiPhone(){}

    @Override
    public void showInfo() {
        System.out.println("I'm huawei.");
    }

}
