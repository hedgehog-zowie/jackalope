package org.jackalope.study.designPattern.factory.simpleFactory;

/**
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class SimpleFactory {

    public static MobilePhone createPhone(Brand brand){
        MobilePhone mobilePhone = null;
        if(brand.equals(Brand.APPLE))
            mobilePhone = new IPhone();
        else if(brand.equals(Brand.HUAWEI))
            mobilePhone = new HuaweiPhone();
        return mobilePhone;
    }

}
