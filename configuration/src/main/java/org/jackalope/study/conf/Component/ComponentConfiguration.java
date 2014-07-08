package org.jackalope.study.conf.component;

import org.jackalope.study.conf.Context;
import org.jackalope.study.conf.*;
import org.jackalope.study.conf.exception.ConfigurationError;
import org.jackalope.study.conf.exception.ConfigurationError.ErrorOrWarning;
import org.jackalope.study.conf.exception.ConfigurationErrorType;
import org.jackalope.study.conf.exception.ConfigurationException;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Nicholas
 *         Email:   nicholas.chen@iuni.com
 */
public abstract class ComponentConfiguration {

    protected String componentName;

    private String type;
    protected boolean configured;
    protected List<ConfigurationError> errors;
    private boolean notFoundConfigClass;

    public boolean isNotFoundConfigClass() {
        return notFoundConfigClass;
    }

    public void setNotFoundConfigClass() {
        this.notFoundConfigClass = true;
    }

    protected ComponentConfiguration(String componentName) {
        this.componentName = componentName;
        errors = new LinkedList<ConfigurationError>();
        this.type = null;
        configured = false;
    }

    public List<ConfigurationError> getErrors() {
        return errors;
    }

    public void configure(Context context) throws ConfigurationException {
        failIfConfigured();
        String confType = context.getString(BasicConfigurationConstants.CONFIG_TYPE);
        if (confType != null && !confType.isEmpty()) {
            this.type = confType;
        }

        if (this.type == null || this.type.isEmpty()) {
            errors.add(new ConfigurationError(componentName,
                    BasicConfigurationConstants.CONFIG_TYPE,
                    ConfigurationErrorType.ATTRS_MISSING, ErrorOrWarning.ERROR));

            throw new ConfigurationException("component has no type. Cannot configure. " + componentName);
        }
    }

    protected void failIfConfigured() throws ConfigurationException {
        if (configured) {
            throw new ConfigurationException("Already configured component." + componentName);
        }
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return toString(0);
    }

    public String toString(int indentCount) {
        StringBuilder indentSb = new StringBuilder("");

        for (int i = 0; i < indentCount; i++) {
            indentSb.append(Configuration.INDENTSTEP);
        }

        String indent = indentSb.toString();
        StringBuilder sb = new StringBuilder(indent);

        sb.append("ComponentConfiguration[").append(componentName).append("]");
        sb.append(Configuration.NEWLINE).append(indent).append(Configuration.INDENTSTEP).append("CONFIG: ");
        sb.append(Configuration.NEWLINE).append(indent).append(Configuration.INDENTSTEP);

        return sb.toString();
    }

    public String getComponentName() {
        return componentName;
    }

    protected void setConfigured() {
        configured = true;
    }

}
