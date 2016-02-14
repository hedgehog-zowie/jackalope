package org.jackalope.study.designPattern.factory.abstractFactory;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class HuaweiPhone implements MobilePhone {

    protected HuaweiPhone(){}

    @Override
    public void showInfo() {
        System.out.println("I'm huawei phone.");
    }

}
