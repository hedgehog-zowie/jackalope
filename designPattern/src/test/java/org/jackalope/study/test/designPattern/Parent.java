package org.jackalope.study.test.designPattern;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Parent {

    public int a = 0;
    public static int sa = 0;

    public void print() {
        System.out.println(Parent.class.getName() + "-print");
    }

    public static void sPrint(){
        System.out.println(Parent.class.getName() + "-sPrint");
    }

}
