/**
 *
 * 责任链模式
 * 使用场景：
 * 1.有多个对象可以处理一个请求，哪个对象处理由运行时决定；
 * 2.在不明确指定接收者的情况下，向多个对象中的一个提交一个请求；
 * 3.可以处理一个请求的对象集合应被动态指定；
 * 包括：
 * 抽象处理者角色；
 * 具体处理者角色；
 * 优点：松耦合，提高灵活性；
 * 缺点：带来一定的性能损耗，每次都从链开头处理；
 *
 * @author zowie
 * Email: nicholas@iuni.com
 */
package org.jackalope.study.designPattern.chinaOfResponsibility;