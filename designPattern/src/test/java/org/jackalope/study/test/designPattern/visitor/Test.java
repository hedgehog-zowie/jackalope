package org.jackalope.study.test.designPattern.visitor;

import junit.framework.TestCase;
import org.jackalope.study.designPattern.visitor.Flower;
import org.jackalope.study.designPattern.visitor.FlowerGenerator;
import org.jackalope.study.designPattern.visitor.StringVal;
import org.jackalope.study.designPattern.visitor.Visitor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class Test extends TestCase {

    List flowers = new ArrayList();

    public Test() {
        for (int i = 0; i < 10; i++)
            flowers.add(FlowerGenerator.newFlower());
    }

    Visitor visitor;

    public void test() {
        visitor = new StringVal();
        Iterator it = flowers.iterator();
        while (it.hasNext()) {
            ((Flower) it.next()).accept(visitor);
            System.out.println(visitor);
        }
    }

}
