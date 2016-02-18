package org.jackalope.study.designPattern.adapter;

/**
 *
 * 适配器
 *
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Circle implements Shape{
    @Override
    public boolean display() {
        AdapteeCircle adapteeCircle = new AdapteeCircle();
        adapteeCircle.display();
        return true;
    }
}
