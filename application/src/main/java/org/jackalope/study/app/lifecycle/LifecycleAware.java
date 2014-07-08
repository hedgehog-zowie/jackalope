package org.jackalope.study.app.lifecycle;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public interface LifecycleAware {
    void start();

    void stop();

    LifecycleState getLifecycleState();
}
