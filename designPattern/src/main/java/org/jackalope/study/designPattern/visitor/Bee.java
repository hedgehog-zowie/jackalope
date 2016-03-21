package org.jackalope.study.designPattern.visitor;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Bee implements Visitor {

    private String s;

    @Override
    public void visit(Flower flower) {
        if (flower instanceof Chrysanthemum) {
            s = "Bee And Chrysanthemum";
        } else if (flower instanceof Gladiolus) {
            s = "Bee And Gladiolus";
        } else if (flower instanceof Runuculus) {
            s = "Bee And Runuculus";
        }
    }

    @Override
    public String toString() {
        return s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}
