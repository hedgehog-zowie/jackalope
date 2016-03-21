package org.jackalope.study.designPattern.visitor;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Chrysanthemum implements Flower {
    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
