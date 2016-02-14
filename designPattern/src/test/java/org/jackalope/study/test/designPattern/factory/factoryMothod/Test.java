package org.jackalope.study.test.designPattern.factory.factoryMothod;

import org.jackalope.study.designPattern.factory.factoryMethod.*;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Test {

    public static void main(String args[]){
        Factory iPhoneFactory = new IPhoneFactory();
        MobilePhone iPhone = iPhoneFactory.create();
        iPhone.showInfo();
        Factory huaweiFactory = new HuaweiFactory();
        MobilePhone huawei = huaweiFactory.create();
        huawei.showInfo();
    }

}
