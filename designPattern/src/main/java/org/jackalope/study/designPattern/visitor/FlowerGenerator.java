package org.jackalope.study.designPattern.visitor;

import java.util.Random;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class FlowerGenerator {

    private static final Random random = new Random();

    public static Flower newFlower() {
        switch (random.nextInt(3)) {
            case 1:
                return new Chrysanthemum();
            case 2:
                return new Gladiolus();
            default:
                return new Runuculus();
        }
    }

}
