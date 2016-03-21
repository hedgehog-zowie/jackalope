package org.jackalope.study.corejava.corejava9.v2ch10.sourceAnnotations;
import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.SOURCE)
public @interface Property
{
   String editor() default ""; 
}
