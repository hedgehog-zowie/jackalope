package org.jackalope.study.test.designPattern;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Child extends Parent {

    public int a = 1;
    public static int sa = 2;

    public void print() {
        System.out.println(Child.class.getName() + "-print");
    }

    public static void sPrint(){
        System.out.println(Child.class.getName() + "-sPrint");
    }

}
