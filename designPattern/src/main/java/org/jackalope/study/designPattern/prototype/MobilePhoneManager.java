package org.jackalope.study.designPattern.prototype;

import java.util.HashMap;
import java.util.Map;

/**
 * 原型管理器
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class MobilePhoneManager {

    private final static Map<String, MobilePhone> mobilePhoneMap = new HashMap<String, MobilePhone>();

    public static MobilePhone getMobilePhone(String mobilePhoneName) throws ClassNotFoundException, IllegalAccessException, InstantiationException, CloneNotSupportedException {
        if (!mobilePhoneMap.containsKey(mobilePhoneName)) {
            MobilePhone mobilePhone = (MobilePhone) Class.forName(mobilePhoneName).newInstance();
            mobilePhoneMap.put(mobilePhoneName, mobilePhone);
        }
        return (MobilePhone) mobilePhoneMap.get(mobilePhoneName).clone();
    }

}
