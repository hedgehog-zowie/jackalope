package org.jackalope.study.conf.common;

import org.jackalope.study.conf.exception.ConfigurationError;

import java.util.LinkedList;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public abstract class AbstractConfiguration {

    protected final LinkedList<ConfigurationError> errors;

    public AbstractConfiguration(){
        errors = new LinkedList<ConfigurationError>();
    }

    protected abstract boolean addRawProperty(String name, String value);

    protected abstract void validateConfiguration();

}
