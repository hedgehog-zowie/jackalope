package org.jackalope.study.designPattern.build;

import java.util.List;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class MobilePhone {

    private String model;
    private List<String> components;

    @Override
    public String toString() {
        return model + ":" + components;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<String> getComponents() {
        return components;
    }

    public void setComponents(List<String> components) {
        this.components = components;
    }
}
