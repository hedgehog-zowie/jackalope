package org.jackalope.study.test.designPattern;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Test {

    public static void main(String args[]){
        Parent t = new Child();
        System.out.println(t.a);
        System.out.println(t.sa);
        t.print();
        t.sPrint();
    }

}
