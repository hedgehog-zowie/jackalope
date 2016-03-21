package org.jackalope.study.designPattern.visitor;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Runuculus implements Flower {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
