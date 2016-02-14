package org.jackalope.study.test.designPattern.factory.abstractFactory;

import org.jackalope.study.designPattern.factory.abstractFactory.*;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Test {

    public static void main(String args[]){
        Factory appleFactory = new AppleFactory();
        MobilePhone iPhone = appleFactory.createPhone();
        Pad iPad = appleFactory.createPad();
        iPhone.showInfo();
        iPad.showInfo();
        Factory huaweiFactory = new HuaweiFactory();
        MobilePhone huaweiPhone = huaweiFactory.createPhone();
        Pad huaweiPad = huaweiFactory.createPad();
        huaweiPhone.showInfo();
        huaweiPad.showInfo();
    }

}
