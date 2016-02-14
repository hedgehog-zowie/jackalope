package org.jackalope.study.designPattern.factory.abstractFactory;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class AppleFactory implements Factory {

    @Override
    public MobilePhone createPhone() {
        return new IPhone();
    }

    @Override
    public Pad createPad() {
        return new IPad();
    }

}
