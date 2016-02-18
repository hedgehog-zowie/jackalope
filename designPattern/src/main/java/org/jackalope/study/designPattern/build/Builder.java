package org.jackalope.study.designPattern.build;

import java.util.List;

/**
 *
 * 抽象创建者角色
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public interface Builder {

    MobilePhone buildPhone(List<String> components);

}
