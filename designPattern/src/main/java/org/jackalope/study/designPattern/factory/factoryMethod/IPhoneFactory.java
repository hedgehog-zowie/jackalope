package org.jackalope.study.designPattern.factory.factoryMethod;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class IPhoneFactory implements Factory {

    public MobilePhone create() {
        return new IPhone();
    }

}
