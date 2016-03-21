package org.jackalope.study.designPattern.visitor;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Gladiolus implements Flower {

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

}
