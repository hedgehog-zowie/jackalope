package org.jackalope.study.test.designPattern.build;

import org.jackalope.study.designPattern.build.AppleBuilder;
import org.jackalope.study.designPattern.build.Director;
import org.jackalope.study.designPattern.build.HuaweiBuilder;
import org.jackalope.study.designPattern.build.MobilePhone;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class TestBuildPattern {

    private List<String> components;

    @Before
    public void prepareComponents() {
        components = Arrays.asList(new String[]{"cpu", "ram", "rom"});
    }

    @Test
    public void testBuildHuawei() {
        Director huaweiDirector = new Director(new HuaweiBuilder());
        MobilePhone mobilePhone = huaweiDirector.getPhone(components);
        assertEquals(mobilePhone.toString(), "huawei:[cpu, ram, rom]");
    }

    @Test
    public void testBuildIphone() {
        Director appleDirector = new Director(new AppleBuilder());
        MobilePhone mobilePhone = appleDirector.getPhone(components);
        assertEquals(mobilePhone.toString(), "iPhone:[cpu, ram, rom]");
    }

}
