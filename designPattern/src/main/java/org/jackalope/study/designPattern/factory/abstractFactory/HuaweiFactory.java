package org.jackalope.study.designPattern.factory.abstractFactory;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class HuaweiFactory implements Factory{

    @Override
    public MobilePhone createPhone() {
        return new HuaweiPhone();
    }

    @Override
    public Pad createPad() {
        return new HuaweiPad();
    }

}
