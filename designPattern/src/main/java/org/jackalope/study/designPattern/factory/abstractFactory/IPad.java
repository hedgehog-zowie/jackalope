package org.jackalope.study.designPattern.factory.abstractFactory;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class IPad implements Pad {

    protected IPad(){}

    @Override
    public void showInfo() {
        System.out.println("I'm iPad.");
    }

}
