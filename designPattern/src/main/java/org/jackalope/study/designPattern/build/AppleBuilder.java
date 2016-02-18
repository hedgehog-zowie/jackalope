package org.jackalope.study.designPattern.build;

import java.util.List;

/**
 *
 * 具体创建者角色
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class AppleBuilder implements Builder {

    @Override
    public MobilePhone buildPhone(List<String> components) {
        IPhone iPhone = new IPhone();
        iPhone.setModel("iPhone");
        iPhone.setComponents(components);
        return iPhone;
    }

}
