package org.jackalope.study.designPattern.visitor;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class StringVal implements Visitor {

    private String s;

    @Override
    public void visit(Flower flower) {
        if(flower instanceof Chrysanthemum){
            s = "Chrysanthemum";
        }else if(flower instanceof Gladiolus){
            s = "Gladiolus";
        }else if(flower instanceof Runuculus){
            s = "Runuculus";
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
