package org.jackalope.study.designPattern.build;

import java.util.List;

/**
 *
 * 具体创建者角色
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class HuaweiBuilder implements Builder {

    @Override
    public MobilePhone buildPhone(List<String> components) {
        HuaweiPhone huaweiPhone = new HuaweiPhone();
        huaweiPhone.setModel("huawei");
        huaweiPhone.setComponents(components);
        return huaweiPhone;
    }

}
