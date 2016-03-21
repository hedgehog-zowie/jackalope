package org.jackalope.study.interview.object;

/**
 * @author zowie
 *         Email: nicholas@iuni.com
 */
public class HashCodeAndEquals {

    public static void main(String args[]) {

        String str1 = "123";
        String str2 = "123";
        System.out.println(str1.equals(str2));
        System.out.println(str1 == str2);
        System.out.println(str1.hashCode());
        System.out.println(str2.hashCode());
        str2 = new String("123");
        System.out.println(str1.equals(str2));
        System.out.println(str1 == str2);
        System.out.println(str1.hashCode());
        System.out.println(str2.hashCode());

    }

}
