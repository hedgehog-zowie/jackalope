/**
 *
 * 观察者模式：
 * 定义对象的一种一对多的关系，当一个对象的状态发生改变时，所有依赖该对象的所有对象自动发生改变；
 * 包括：
 * 抽象目标角色；
 * 具体目标角色；
 * 抽象观察者角色；
 * 具体观察者角色；
 * 使用场景：
 * 1.一个抽象模型有两个方面，其中一个方面依赖于另一个方面；
 * 2.当一个对象的状态改变时，其他对象也需要改变；
 * 3.一个对象必须通知其他对象，却不知道其他对象是谁（松耦合）；
 *
 * @author zowie
 * Email: nicholas@iuni.com
 */
package org.jackalope.study.designPattern.observer;