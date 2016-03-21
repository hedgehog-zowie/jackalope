package org.jackalope.study.designPattern.visitor;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public interface Flower {

    void accept(Visitor visitor);

}
