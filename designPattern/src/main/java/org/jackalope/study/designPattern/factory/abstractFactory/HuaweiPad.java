package org.jackalope.study.designPattern.factory.abstractFactory;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class HuaweiPad implements Pad {

    protected HuaweiPad(){}

    @Override
    public void showInfo() {
        System.out.println("I'm huawei pad.");
    }

}
