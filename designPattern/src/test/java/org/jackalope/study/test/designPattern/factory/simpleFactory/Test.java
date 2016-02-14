package org.jackalope.study.test.designPattern.factory.simpleFactory;

import org.jackalope.study.designPattern.factory.simpleFactory.Brand;
import org.jackalope.study.designPattern.factory.simpleFactory.MobilePhone;
import org.jackalope.study.designPattern.factory.simpleFactory.SimpleFactory;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Test {

    public static void main(String args[]){

        MobilePhone iPhone = SimpleFactory.createPhone(Brand.APPLE);
        iPhone.showInfo();
        MobilePhone huaWei = SimpleFactory.createPhone(Brand.HUAWEI);
        huaWei.showInfo();

    }

}
