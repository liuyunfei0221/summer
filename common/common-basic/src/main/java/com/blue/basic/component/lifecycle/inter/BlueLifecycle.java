package com.blue.basic.component.lifecycle.inter;

/**
 * Used to handle the interface constraints of the bean state after the context is initialized,
 * The class that implements this interface will be automatically injected into the container and
 * will execute start after the program starts according to the priority order, and execute stop at the end of the program.
 * Generally used for elegant online and offline processing during smooth publishing
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface BlueLifecycle {

    /**
     * order for start
     *
     * @return
     */
    int startPrecedence();

    /**
     * order for stop
     *
     * @return
     */
    int stopPrecedence();

    /**
     * start action
     */
    void start();

    /**
     * stop action
     */
    void stop();

}
